package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;
import ru.bandit.cryptobot.repositories.AllowedCurrenciesRepository;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotService {

    public static final short OK = 1;
    public static final short NOT_FOUND_CHAT = 2;
    public static final short NOT_FOUND_SUBSCRIPTION = 3;
    public static final short ALREADY_IN_CHAT = 4;
    public static final short ALREADY_SUBSCRIBED = 5;
    public static final short NOT_FOUND_CURRENCY = 6;
    public static final short NO_SUBSCRIPTIONS = 7;

    private static final String USER_NOT_FOUND = "User {} not found";

    //TODO change this to currencies repository
    private final List<String> availableCurrencies = List.of("BTCRUB", "ETHRUB", "USDTRUB", "BNBRUB", "XRPRUB", "ADARUB");

    Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    @Autowired
    AllowedCurrenciesRepository currenciesRepository;

    public short addNewUser(long chatId) {
        ChatEntity newChat = activeChatsRepository.findByChatName(chatId);

        if (newChat == null) {
            newChat = new ChatEntity();
            newChat.setChatName(chatId);
            activeChatsRepository.save(newChat);
            logger.debug("Successfully subscribed user {}.", chatId);
            return OK;
        } else {
            logger.trace("User {} already in chat.", chatId);
            return ALREADY_IN_CHAT;
        }
    }

    //FIXME currencies must be in collection, not in string
    public short unsubscribe(long chatId, String currencies) {

        logger.trace("Trying to unsubscribe user {}", chatId);

        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.trace(USER_NOT_FOUND, chatId);
            return NOT_FOUND_CHAT;
        }

        MailingListEntity mailingListEntity = mailingListRepository.findByChatAndCurrency(chat, currencies);
        if (mailingListEntity == null) {
            logger.trace("User {} already unsubscribed from {}", chatId, currencies);
            return NOT_FOUND_SUBSCRIPTION;
        }

        mailingListRepository.delete(mailingListEntity);
        logger.trace("User {} unsubscribed from {} successfully.", chatId, currencies);
        return OK;
    }

    public short unsubscribeAll(long chatId) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug(USER_NOT_FOUND, chatId);
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

    public short pauseUser(long chatId) {
        //TODO implement this
        return OK;
    }

    public short resumeUser(long chatId) {
        //TODO implement this
        return OK;
    }

    public List<String> getAllSubscriptions(long chatId) {
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        List<MailingListEntity> subscriptionsList = mailingListRepository.findByChat(chat);
        //TODO make this list user friendly
        return subscriptionsList.stream().map(MailingListEntity::toString).collect(Collectors.toList());
    }

    public short createTrigger(long chatId, List<String> params) {
        //todo implement this
        return OK;
    }

    public short createAverage(long chatId, List<String> params) {
        //todo implement this
        return OK;
    }

    public short createSimple(long chatId, String figi) {
        //todo refactor this
        ChatEntity chat = activeChatsRepository.findByChatName(chatId);
        if (chat == null) {
            logger.debug(USER_NOT_FOUND, chatId);
            addNewUser(chatId);
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

    public String getOnce(String currency1, String currency2) {
        //todo implement this
        return "currency rates";
    }

    public List<String> getAllCurrenciesList() {
        //todo
        List<String> allCurrenciesList = new ArrayList<>(currenciesRepository.findAllCurrencies());
        allCurrenciesList.addAll(currenciesRepository.findAllCrypto());
        return allCurrenciesList;
    }
}
