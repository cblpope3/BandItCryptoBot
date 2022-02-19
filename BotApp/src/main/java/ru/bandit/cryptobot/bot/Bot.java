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
import ru.bandit.cryptobot.bot.menu.BotMenuMain;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;

@Component
public class Bot extends TelegramLongPollingBot {

    Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String token;
    private final String username;

    @Autowired
    BotRequestProcessor requestProcessor;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    Bot(@Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            //received text message

            logger.debug("Got text message from {}: {}", update.getMessage().getChatId(), update.getMessage().getText());

            SendMessage sendMessage = new SendMessage();

            String text = requestProcessor.processTextRequest(update.getMessage());

            BotMenuMain aaa = new BotMenuMain();
            sendMessage.setReplyMarkup(aaa.getMarkup(null, null));
            sendMessage.setText(text);
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage(sendMessage);

        } else if (update.hasCallbackQuery()) {
            // Set variables

            EditMessageText newMessage = requestProcessor.processCallbackRequest(update.getCallbackQuery());
            newMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

            try {
                execute(newMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDataToSubscribers(Long chatName, String data) {
        sendMessage(chatName, data);
    }

    private void sendMessage(Long chatName, String message) {
        //TODO maybe this method is redundant
        SendMessage sendingMessage = new SendMessage(chatName.toString(), message);
        try {
            execute(sendingMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMessage(SendMessage sendingMessage) {
        //TODO maybe this method is redundant
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