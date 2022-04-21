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
import ru.bandit.cryptobot.bot.menu.AbstractMenuItem;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.services.MetricsService;
import ru.bandit.cryptobot.services.StreamService;

import java.util.List;
import java.util.Map;

@Component
public class Bot extends TelegramLongPollingBot {

    private final String token;
    private final String username;
    Logger logger = LoggerFactory.getLogger(Bot.class);

    @Autowired
    StreamService streamService;

    @Autowired
    MetricsService metricsService;

    @Autowired
    BotRequestHandler botRequestHandler;

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

            String incomingRequest = update.getMessage().getText();

            //counting metrics
            metricsService.incrementTextCommandCounter();

            UserDTO userDTO = new UserDTO(update.getMessage().getFrom(), update.getMessage().getChatId(),
                    update.getMessage().getMessageId());

            //get markup and message text
            AbstractMenuItem response = botRequestHandler.getMenuLayout(incomingRequest, userDTO);

            replyMessage.setChatId(update.getMessage().getChatId().toString());
            replyMessage.setReplyMarkup(response.getMarkup());
            replyMessage.setText(response.getText());

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
            metricsService.incrementInteractiveCommandCounter();

            //preparing incoming request
            String incomingRequest = update.getCallbackQuery().getData();

            UserDTO userDTO = new UserDTO(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getMessageId());

            //get markup and message text
            AbstractMenuItem response = botRequestHandler.getMenuLayout(incomingRequest, userDTO);

            //preparing response
            replyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            replyMessage.setReplyMarkup(response.getMarkup());
            replyMessage.setText(response.getText());
            replyMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

            try {
                execute(replyMessage);
            } catch (TelegramApiException e) {
                logger.error("Error while trying to edit message: {}", e.getMessage());
            }
        }
    }

    //todo write javadoc
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

    //todo write javadoc
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