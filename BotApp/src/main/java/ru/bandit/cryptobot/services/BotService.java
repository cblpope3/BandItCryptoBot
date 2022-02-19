package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.*;
import ru.bandit.cryptobot.repositories.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotService {

    public static final short OK = 1;
    public static final short NOT_FOUND_USER = 2;
    public static final short NOT_FOUND_SUBSCRIPTION = 3;
    public static final short ALREADY_IN_CHAT = 4;
    public static final short ALREADY_SUBSCRIBED = 5;
    public static final short NOT_FOUND_CURRENCY = 6;
    public static final short NO_SUBSCRIPTIONS = 7;

    private static final String USER_NOT_FOUND_MESSAGE = "User {} not found";

    Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    TriggerTypeRepository triggerTypeRepository;

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrencyPairRepository currencyPairRepository;

    public short addNewUser(long chatId, String username) {
        UserEntity newChat = usersRepository.findByChatId(chatId);

        if (newChat == null) {
            newChat = new UserEntity();
            newChat.setChatId(chatId);
            newChat.setChatName(username);
            newChat.setPaused(false);
            usersRepository.save(newChat);
            logger.debug("Successfully subscribed user {}.", chatId);
            return OK;
        } else {
            logger.trace("User {} already in chat.", chatId);
            return ALREADY_IN_CHAT;
        }
    }

    public short unsubscribe(Long triggerId) {
        logger.trace("Trying to remove trigger #{}", triggerId);

        UserTriggerEntity userTrigger = userTriggersRepository.findById(triggerId);
        if (userTrigger == null) {
            logger.trace("Trigger #{} doesn't exist", triggerId);
            return NOT_FOUND_SUBSCRIPTION;
        } else {
            userTriggersRepository.delete(userTrigger);
            logger.trace("Trigger #{} deleted successfully.", triggerId);
            return OK;
        }

    }

    public short unsubscribeAll(Long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        List<UserTriggerEntity> foundSubscriptions = userTriggersRepository.findByUser(user);

        if (foundSubscriptions == null) {
            logger.debug("Subscriptions for user {} not found.", chatId);
            return NOT_FOUND_SUBSCRIPTION;
        } else {
            userTriggersRepository.deleteAll(foundSubscriptions);
            logger.debug("User {} unsubscribed from all subscriptions successfully.", chatId);
            return OK;
        }
    }

    public short pauseUser(long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        } else {
            user.setPaused(true);
            usersRepository.save(user);
            logger.debug("User {} paused successfully.", chatId);
            return OK;
        }
    }

    public short resumeUser(long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        } else {
            user.setPaused(false);
            usersRepository.save(user);
            logger.debug("User {} resumed successfully.", chatId);
            return OK;
        }
    }

    public List<String> getAllSubscriptions(long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return Collections.emptyList();
        } else {
            List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(user);
            if (subscriptionsList == null) {
                logger.debug("Not found any triggers for user {}.", chatId);
                return Collections.emptyList();
            } else {
                logger.trace("Returned subscriptions of user {}", chatId);
                //FIXME work on this stream
                return subscriptionsList.stream()
                        .map(a -> a.toString())
                        .collect(Collectors.toList());
            }
        }
    }

    public short createTarget(long chatId, List<String> params) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        //toDO implement params list check
        //todo what happens if data not found in repo?
        CurrencyEntity currency1 = currencyRepository.findByCurrencyNameUser(params.remove(0));
        CurrencyEntity currency2 = currencyRepository.findByCurrencyNameUser(params.remove(0));

        CurrencyPairEntity currencyPair = currencyPairRepository.findByCurrency1AndCurrency2(currency1, currency2);

        //todo deal with trigger names
        TriggerTypeEntity triggerType = triggerTypeRepository.findByTriggerName("target");

        UserTriggerEntity userTrigger = new UserTriggerEntity();
        userTrigger.setUser(user);

        userTrigger.setCurrencyPair(currencyPair);

        userTrigger.setTriggerType(triggerType);

        userTrigger.setTargetValue(Integer.parseInt(params.remove(0)));

        userTriggersRepository.save(userTrigger);

        return OK;
    }

    public short createAverage(long chatId, List<String> params) {
        //todo implement this
        return OK;
    }

    public short createSimple(long chatId, List<String> params) {
        //todo refactor this
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        //toDO implement params list check
        //todo what happens if data not found in repo?
        CurrencyEntity currency1 = currencyRepository.findByCurrencyNameUser(params.remove(0));
        CurrencyEntity currency2 = currencyRepository.findByCurrencyNameUser(params.remove(0));

        CurrencyPairEntity currencyPair = currencyPairRepository.findByCurrency1AndCurrency2(currency1, currency2);

        //todo deal with trigger names
        TriggerTypeEntity triggerType = triggerTypeRepository.findByTriggerName("simple");

        UserTriggerEntity userTrigger = new UserTriggerEntity();
        userTrigger.setUser(user);

        userTrigger.setCurrencyPair(currencyPair);

        userTrigger.setTriggerType(triggerType);

        userTrigger.setTargetValue(Integer.parseInt(params.remove(0)));

        userTriggersRepository.save(userTrigger);

        return OK;
    }

    public String getOnce(String currency1, String currency2) {
        //todo implement this
        return "some currency rates";
    }

    public List<String> getAllCurrenciesList() {

        List<CurrencyEntity> allCurrenciesList = currencyRepository.findAll();
        //todo refactor this stream
        return allCurrenciesList.stream().map(CurrencyEntity::toString).collect(Collectors.toList());
    }
}
