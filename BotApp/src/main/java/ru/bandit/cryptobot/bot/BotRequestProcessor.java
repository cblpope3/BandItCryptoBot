package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class BotRequestProcessor {

    public static final int OK = 1;
    public static final int NOT_FOUND_CHAT = 2;
    public static final int NOT_FOUND_SUBSCRIPTION = 3;
    public static final int ALREADY_IN_CHAT = 4;
    public static final int ALREADY_SUBSCRIBED = 5;
    public static final int NOT_FOUND_CURRENCY = 6;

    private final List<String> availableCurrencies = List.of("BTCRUB", "ETHRUB", "USDTRUB", "BNBRUB", "XRPRUB", "ADARUB");

    Logger logger = LoggerFactory.getLogger(BotRequestProcessor.class);

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    public String processRequest(Message message) {
        long chatId = message.getChatId();

        List<String> separatedMessage = Arrays.asList(message.getText().split(" "));

        logger.trace("Received command list: {}",separatedMessage);
        int commandStatus;
        switch (separatedMessage.get(0)) {
            case "/start":
                logger.debug("Got start command from {}", chatId);
                commandStatus = appendChatNewcomer(chatId);
                if (commandStatus == OK) return "Привет! Я бот от команды BandIt! Для помощи напиши /help";
                else if (commandStatus == ALREADY_IN_CHAT) return "Ещё раз привет!";
                break;
            case "/stop":
                logger.debug("Got stop command from {}", chatId);
                commandStatus = removeChatMember(chatId);
                if (commandStatus == OK) return "До встречи! Пиши /start, если соскучишься...";
                else if (commandStatus == NOT_FOUND_CHAT) return "Уже прощались.";
                break;
            case "/help":
                logger.debug("Got help command from {}", chatId);
                return "Чтобы подписаться на рассылку курса валюты напиши \"/subscribe BTCRUB\" \n" +
                        "Чтобы отписаться, напиши \"/unsubscribe BTCRUB\"\n" +
                        "Поддерживаемые валюты: BTCRUB, ETHRUB, USDTRUB, BNBRUB, XRPRUB, ADARUB\n" +
                        "Чтобы прекратить общение, напиши /stop";
            case "/subscribe":
                logger.debug("Got subscribe to {} command from {}", separatedMessage.get(0), chatId);
                if (separatedMessage.size() == 1) return "Повторите с указанием валюты.";
                commandStatus = subscribe(chatId, separatedMessage.get(1));
                if (commandStatus == OK) return "Подписались на " + separatedMessage.get(1);
                else if (commandStatus == NOT_FOUND_CHAT) return "Сначала наберите команду /start";
                else if (commandStatus == ALREADY_SUBSCRIBED) return "Уже подписаны на эту рассылку";
                else if (commandStatus == NOT_FOUND_CURRENCY)
                    return "Такой валюты я не знаю: " + separatedMessage.get(1);
                break;
            case "/unsubscribe":
                logger.debug("Got unsubscribe from {} command from {}", separatedMessage.get(0), chatId);
                commandStatus = unsubscribe(chatId, separatedMessage.get(1));
                if (commandStatus == OK) return "Отписались от рассылки на " + separatedMessage.get(1);
                else if (commandStatus == NOT_FOUND_CHAT) return "Сначала наберите команду /start";
                else if (commandStatus == NOT_FOUND_SUBSCRIPTION)
                    return "Вы ещё не подписались на " + separatedMessage.get(1);
                break;
            default:
                return "Неизвестная команда";
        }

        logger.error("Error in switch-case. Processing message from {} is: {}. \nSeparated message: {}",
                message.getChatId(), message.getText(), separatedMessage);
        return "Что-то пошло не так";
    }

    public int appendChatNewcomer(long chatId) {

        ChatEntity newChat = activeChatsRepository.findByChatName(chatId);

        if (newChat == null) {
            newChat = new ChatEntity();
            newChat.setChatName(chatId);
            activeChatsRepository.save(newChat);
            logger.debug("Successfully subscribed user {}.",chatId);
            return OK;
        } else {
            logger.debug("User {} already in chat.",chatId);
            return ALREADY_IN_CHAT;
        }
    }

    public int removeChatMember(long chatId) {
        ChatEntity chatEntity = activeChatsRepository.findByChatName(chatId);
        if (chatEntity == null) {
            logger.debug("User {} already stopped chat.", chatId);
            return NOT_FOUND_CHAT;
        }

        unsubscribeAll(chatId);
        //activeChatsRepository.delete(chatEntity);
        logger.debug("User {} successfully stopped chat.", chatId);
        return OK;
    }

    public int subscribe(long chatId, String figi) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug("User {} not found", chatId);
            //return NOT_FOUND_CHAT;
            appendChatNewcomer(chatId);
        }

        MailingListEntity newMailingRecord = mailingListRepository.findByChatAndCurrency(chat, figi);
        if (newMailingRecord != null) {
            logger.debug("User {} already subscribed to {}", chatId, figi);
            return ALREADY_SUBSCRIBED;
        }

        if (!availableCurrencies.contains(figi)) {
            logger.debug("Currency {} is not supported.", figi);
            return NOT_FOUND_CURRENCY;
        }

        newMailingRecord = new MailingListEntity();
        newMailingRecord.setChat(chat);
        newMailingRecord.setCurrency(figi);

        mailingListRepository.save(newMailingRecord);
        logger.debug("User {} subscribed to {} successfully.", chatId, figi);
        return OK;
    }

    public int unsubscribe(long chatId, String figi) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug("User {} not found",chatId);
            return NOT_FOUND_CHAT;
        }

        MailingListEntity mailingListEntity = mailingListRepository.findByChatAndCurrency(chat, figi);
        if (mailingListEntity == null) {
            logger.debug("User {} already unsubscribed from {}", chatId, figi);
            return NOT_FOUND_SUBSCRIPTION;
        }

        mailingListRepository.delete(mailingListEntity);
        logger.debug("User {} unsubscribed from {} successfully.", chatId, figi);
        return OK;
    }

    public int unsubscribeAll(long chatId) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug("User {} not found", chatId);
            return NOT_FOUND_CHAT;
        }
        List<MailingListEntity> foundSubscriptions = mailingListRepository.findByChat(chat);

        if (foundSubscriptions == null) {
            logger.debug("Subscriptions for user {} not found.", chatId);
            return NOT_FOUND_SUBSCRIPTION;
        }

        mailingListRepository.deleteAll(foundSubscriptions);
        logger.debug("User {} unsubscribed from all subscriptions successfully.", chatId);
        return OK;
    }

}
