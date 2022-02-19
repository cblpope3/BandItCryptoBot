package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Bot extends TelegramLongPollingBot {

    Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String token;
    private final String username;

    @Autowired
    BotRequestProcessor requestProcessor;

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
                logger.warn("Ignoring request because it is not a command: {}", incomingRequest);
                return;
            }

            //get markup and message text
            BotResponse responseTemplate = requestProcessor.generateResponse(processQuery(incomingRequest),
                    update.getMessage().getChatId(),
                    update.getMessage().getFrom().getUserName());

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

            //preparing incoming request
            String incomingRequest = update.getCallbackQuery().getData().toUpperCase();

            //get markup and message text
            BotResponse responseTemplate = requestProcessor.generateResponse(processQuery(incomingRequest),
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getFrom().getUserName());

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

    public void sendDataToSubscribers(Long chatName, String data) {
        //TODO maybe this method is redundant
        SendMessage sendingMessage = new SendMessage(chatName.toString(), data);
        try {
            execute(sendingMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
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