package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.List;

@Service
public class BotService {

    public static final int OK = 1;
    public static final int NOT_FOUND_CHAT = 2;
    public static final int NOT_FOUND_SUBSCRIPTION = 3;
    public static final int ALREADY_IN_CHAT = 4;
    public static final int ALREADY_SUBSCRIBED = 5;
    public static final int NOT_FOUND_CURRENCY = 6;

    //TODO change this to currencies repository
    private final List<String> availableCurrencies = List.of("BTCRUB", "ETHRUB", "USDTRUB", "BNBRUB", "XRPRUB", "ADARUB");

    Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    public int appendChatNewcomer(long chatId) {

        ChatEntity newChat = activeChatsRepository.findByChatName(chatId);

        if (newChat == null) {
            newChat = new ChatEntity();
            newChat.setChatName(chatId);
            activeChatsRepository.save(newChat);
            logger.debug("Successfully subscribed user {}.", chatId);
            return OK;
        } else {
            logger.debug("User {} already in chat.", chatId);
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
        logger.debug("User {} successfully stopped chat.", chatId);
        return OK;
    }

    public int subscribe(long chatId, String figi) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug("User {} not found", chatId);
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
            logger.debug("User {} not found", chatId);
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
