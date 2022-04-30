package ru.bandit.cryptobot.clients;

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
import ru.bandit.cryptobot.dto.BotResponseDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.services.MetricsService;
import ru.bandit.cryptobot.services.QueryService;

/**
 * Class for interaction with Telegram bot api.
 */
@Component
public class TelegramClient extends TelegramLongPollingBot {

    private final String token;
    private final String username;
    private final Logger logger = LoggerFactory.getLogger(TelegramClient.class);

    @Autowired
    MetricsService metricsService;

    @Autowired
    QueryService queryService;

    TelegramClient(@Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.token = token;
        this.username = username;
    }

    /**
     * This method is automatically called when update is received.
     *
     * @param update update content.
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            //received text message
            if (logger.isDebugEnabled())
                logger.debug("Got text message from {}: {}", update.getMessage().getChatId(), update.getMessage().getText());

            this.processTextCommand(update);

        } else if (update.hasCallbackQuery()) {
            //received button press
            if (logger.isDebugEnabled())
                logger.debug("Got button press from {}: {}", update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());

            this.processButtonPress(update);
        }
    }

    /**
     * Method used to send message to user.
     *
     * @param message message to send.
     */
    public void sendMessage(SendMessage message) {
        try {
            execute(message);
            if (logger.isTraceEnabled()) logger.trace("New message sent to chat #{}.", message.getChatId());
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method that processing text commands
     *
     * @param update received message.
     */
    private void processTextCommand(Update update) {

        //counting metrics
        metricsService.incrementTextCommandCounter();

        String incomingRequest = update.getMessage().getText();
        UserDTO userDTO = new UserDTO(update.getMessage().getFrom(), update.getMessage().getChatId(),
                update.getMessage().getMessageId());

        //get markup and message text
        BotResponseDTO response = queryService.makeResponseToUser(userDTO, incomingRequest);

        SendMessage replyMessage = new SendMessage();

        replyMessage.setChatId(update.getMessage().getChatId().toString());
        replyMessage.setReplyMarkup(response.getKeyboard());
        replyMessage.setText(response.getMessage());

        this.sendMessage(replyMessage);
    }

    /**
     * Method that processing button commands.
     *
     * @param update received button press.
     */
    private void processButtonPress(Update update) {

        //calculating metrics
        metricsService.incrementInteractiveCommandCounter();

        //preparing incoming request
        String incomingRequest = update.getCallbackQuery().getData();
        UserDTO userDTO = new UserDTO(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId());

        //get markup and message text
        BotResponseDTO response = queryService.makeResponseToUser(userDTO, incomingRequest);

        //preparing response
        EditMessageText replyMessage = new EditMessageText();

        replyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        replyMessage.setReplyMarkup(response.getKeyboard());
        replyMessage.setText(response.getMessage());
        replyMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        try {
            execute(replyMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while trying to edit message: {}", e.getMessage());
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