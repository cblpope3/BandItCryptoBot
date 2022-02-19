package ru.bandit.cryptobot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;

@Component
public class Bot extends TelegramLongPollingBot {

    private final String token;
    private final String username;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    Bot(@Value("${bot.token}") String token, @Value("${bot.username}") String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            System.out.println("new bot query: " + update.getMessage().getText());
            System.out.println("query came from: " + update.getMessage().getChatId().toString());

            System.out.println("adding new user to repository.");
            ChatEntity newChat = new ChatEntity();
            newChat.setChatName(update.getMessage().getChatId());
            activeChatsRepository.save(newChat);
            System.out.println("chats in database: ");
            for (ChatEntity chat : activeChatsRepository.findAll()) {
                System.out.println(chat.toString());
            }

            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToMe(String newRates) {
        SendMessage message = new SendMessage();

        message.setChatId("637280094");
        message.setText(newRates);

        try {
            execute(message);
        } catch (TelegramApiException e) {
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