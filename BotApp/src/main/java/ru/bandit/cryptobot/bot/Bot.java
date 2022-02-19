package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bandit.cryptobot.entities.MetricsEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.MetricsRepository;
import ru.bandit.cryptobot.services.StreamService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Bot extends TelegramLongPollingBot {

    private final String token;
    private final String username;
    Logger logger = LoggerFactory.getLogger(Bot.class);
    @Autowired
    BotRequestProcessor requestProcessor;

    @Autowired
    StreamService streamService;

    @Autowired
    MetricsRepository metricsRepository;

    Bot(@Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            //received text message
            logger.debug("Got text message from {}: {}", update.getMessage().getChatId(), update.getMessage().getText());
            SendMessage replyMessage = new SendMessage();

            //validating incoming request
            String incomingRequest = update.getMessage().getText().toUpperCase();
            if (!incomingRequest.matches("(/)[A-Z0-9_/]+")) {

                MetricsEntity metrics = metricsRepository.findById(1L);
                if (metrics == null) metrics = new MetricsEntity();
                metrics.setTextCommandCount(metrics.getTextCommandCount());
                metricsRepository.save(metrics);
                logger.debug("Ignoring request because it is not a command: {}", incomingRequest);

                replyMessage.setChatId(update.getMessage().getChatId().toString());
                replyMessage.setText("В команде есть неподдерживаемые символы. Напишите /help для вызова списка возможных команд.");

                try {
                    execute(replyMessage);
                } catch (TelegramApiException e) {
                    logger.error("Error while trying to send new message: {}", e.getMessage());
                }
                return;
            }

            //get markup and message text
            BotResponse responseTemplate = requestProcessor.generateResponse(processQuery(incomingRequest),
                    update.getMessage().getChatId(),
                    update.getMessage().getFrom().getFirstName());

            replyMessage.setChatId(update.getMessage().getChatId().toString());
            replyMessage.setReplyMarkup(responseTemplate.getKeyboard());
            replyMessage.setText(responseTemplate.getMessage());

            try {
                execute(replyMessage);
            } catch (TelegramApiException e) {
                logger.error("Error while trying to send new message: {}", e.getMessage());
            }

        } else if (update.hasCallbackQuery()) {
            //received button press
            logger.trace("Got button press from {}: {}", update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
            EditMessageText replyMessage = new EditMessageText();

            //calculating metrics
            MetricsEntity metrics = metricsRepository.findById(1L);
            if (metrics == null) metrics = new MetricsEntity();
            metrics.setInteractiveCommandCount(metrics.getInteractiveCommandCount());
            metricsRepository.save(metrics);

            //preparing incoming request
            String incomingRequest = update.getCallbackQuery().getData().toUpperCase();

            //get markup and message text
            BotResponse responseTemplate = requestProcessor.generateResponse(processQuery(incomingRequest),
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getFrom().getFirstName());

            //preparing response
            replyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            replyMessage.setReplyMarkup(responseTemplate.getKeyboard());
            replyMessage.setText(responseTemplate.getMessage());
            replyMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

            try {
                execute(replyMessage);
            } catch (TelegramApiException e) {
                logger.error("Error while trying to edit message: {}", e.getMessage());
            }
        }
    }

    private List<String> processQuery(String query) {
        return Arrays.stream(query.split("/"))
                .filter(Objects::nonNull)
                .filter(a -> !a.isEmpty())
                .collect(Collectors.toList());
    }

    public void sendWorkedTargetTriggerToUser(UserTriggerEntity userTrigger, String value) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(userTrigger.getUser().getChatId().toString());
        replyMessage.setText(String.format("Сработал ваш будильник по валюте %s/%s! Текущий курс: %s.",
                userTrigger.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                userTrigger.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                value));
        try {
            execute(replyMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while trying to send new message: {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void sendStreams() {
        Map<Long, List<String>> mailingList = streamService.getStreams();
        SendMessage sendMessage = new SendMessage();
        for (Map.Entry<Long, List<String>> stream : mailingList.entrySet()) {
            sendMessage.setChatId(stream.getKey().toString());
            sendMessage.setText(String.join("\n", stream.getValue()));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}