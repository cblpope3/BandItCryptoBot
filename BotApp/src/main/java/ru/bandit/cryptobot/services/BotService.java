package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.entities.*;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.List;
import java.util.Set;
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
    public static final short NOT_FOUND_TRIGGER_TYPE = 8;
    public static final short NOT_VALID_PARAMETER = 9;

    private static final String USER_NOT_FOUND_MESSAGE = "User {} not found";

    Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    TriggersService triggersService;

    @Autowired
    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    public short unsubscribe(Long chatId, Long triggerId) {
        logger.trace("Trying to remove trigger #{}", triggerId);

        UserTriggerEntity userTrigger = userTriggersRepository.findById(triggerId);
        if (userTrigger == null) {
            logger.trace("Trigger #{} doesn't exist", triggerId);
            return NOT_FOUND_SUBSCRIPTION;
        } else if (!userTrigger.getUser().getChatId().equals(chatId)) {
            logger.warn("User {} trying to delete foreign subscription.", chatId);
            return NOT_FOUND_SUBSCRIPTION;
        } else {
            if (userTrigger.getTriggerType().getTriggerName().equals("target-up") ||
                    userTrigger.getTriggerType().getTriggerName().equals("target-down")) {
                triggersService.deleteTargetTrigger(userTrigger.getId());
            }
            userTriggersRepository.delete(userTrigger);
            logger.trace("Trigger #{} deleted successfully.", triggerId);
            return OK;
        }

    }

    public short unsubscribeAll(Long chatId) {
        UserEntity user = usersService.getUser(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        List<UserTriggerEntity> foundSubscriptions = userTriggersRepository.findByUser(user);

        if (foundSubscriptions == null || foundSubscriptions.isEmpty()) {
            logger.debug("Subscriptions for user {} not found.", chatId);
            return NO_SUBSCRIPTIONS;
        } else {
            for (UserTriggerEntity triggerEntity : foundSubscriptions) {
                if (triggerEntity.getTriggerType().getTriggerName().equals("target-up") ||
                        triggerEntity.getTriggerType().getTriggerName().equals("target-down")) {
                    triggersService.deleteTargetTrigger(triggerEntity.getId());
                }
            }
            userTriggersRepository.deleteAll(foundSubscriptions);
            logger.debug("User {} unsubscribed from all subscriptions successfully.", chatId);
            return OK;
        }
    }

    public String getAllSubscriptions(long chatId) {
        UserEntity user = usersService.getUser(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return "";
        } else {
            List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(user);
            if (subscriptionsList == null) {
                logger.debug("Not found any triggers for user {}.", chatId);
                return "";
            } else {
                logger.trace("Returned subscriptions of user {}", chatId);
                return subscriptionsList.stream()
                        //FIXME subscription name is not fine for simple triggers
                        .map(a -> String.format("№%d - %s/%s - %s - %d",
                                a.getId(),
                                a.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                                a.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                                a.getTriggerType().getTriggerName(),
                                a.getTargetValue()))
                        .collect(Collectors.joining("\n"));
            }
        }
    }

    public short createTarget(long chatId, List<String> params) {

        if (params.size() < 4) {
            logger.warn("not enough parameters");
            return NOT_VALID_PARAMETER;
        } else if (!params.get(3).matches("[0-9]+")) {
            logger.warn("average period is not integer");
            return NOT_VALID_PARAMETER;
        }

        UserEntity user = usersService.getUser(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(params.remove(0), params.remove(0));
        if (currencyPair == null) {
            return NOT_FOUND_CURRENCY;
        }

        TriggerTypeEntity triggerType;
        String triggerTypeName = params.remove(0);
        if (triggerTypeName.equals("UP")) triggerType = triggersService.getTriggerUp();
        else if (triggerTypeName.equals("DOWN")) triggerType = triggersService.getTriggerDown();
        else {
            logger.warn("trigger type not recognized");
            return NOT_VALID_PARAMETER;
        }
        if (triggerType == null) {
            logger.error("Not found target trigger in database.");
            return NOT_FOUND_TRIGGER_TYPE;
        }

        //fixme target value is not in %
        Integer targetVal = Integer.parseInt(params.remove(0));

        UserTriggerEntity userTrigger = new UserTriggerEntity();
        userTrigger.setUser(user);
        userTrigger.setCurrencyPair(currencyPair);
        userTrigger.setTriggerType(triggerType);
        userTrigger.setTargetValue(targetVal);

        List<UserTriggerEntity> existingTriggerList = userTriggersRepository.findByUser(user);
        existingTriggerList.forEach(a -> a.setId(null));
        if (existingTriggerList.contains(userTrigger)) {
            logger.debug("User already subscribed to this trigger");
            return ALREADY_SUBSCRIBED;
        }

        userTriggersRepository.save(userTrigger);
        triggersService.createTargetTrigger(userTrigger);
        return OK;
    }

    public short createAverage(long chatId, List<String> params) {

        if (params.size() < 2) {
            logger.warn("not enough parameters");
            return NOT_VALID_PARAMETER;
        }

        UserEntity user = usersService.getUser(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(params.remove(0), params.remove(0));
        if (currencyPair == null) {
            return NOT_FOUND_CURRENCY;
        }

        TriggerTypeEntity triggerType = triggersService.getCustomTriggerType("average");
        if (triggerType == null) {
            logger.error("Not found average trigger in database.");
            return NOT_FOUND_TRIGGER_TYPE;
        }

        UserTriggerEntity userTrigger = new UserTriggerEntity();
        userTrigger.setUser(user);
        userTrigger.setCurrencyPair(currencyPair);
        userTrigger.setTriggerType(triggerType);

        List<UserTriggerEntity> existingTriggerList = userTriggersRepository.findByUser(user);
        existingTriggerList.forEach(a -> a.setId(null));
        if (existingTriggerList.contains(userTrigger)) {
            logger.debug("User already subscribed to this trigger");
            return ALREADY_SUBSCRIBED;
        }

        userTriggersRepository.save(userTrigger);

        return OK;
    }

    public short createSimple(long chatId, List<String> params) {

        if (params.size() < 2) {
            logger.warn("not enough parameters");
            return NOT_VALID_PARAMETER;
        }

        UserEntity user = usersService.getUser(chatId);
        if (user == null) {
            logger.warn(USER_NOT_FOUND_MESSAGE, chatId);
            return NOT_FOUND_USER;
        }

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(params.remove(0), params.remove(0));
        if (currencyPair == null) {
            return NOT_FOUND_CURRENCY;
        }

        TriggerTypeEntity triggerType = triggersService.getCustomTriggerType("simple");
        if (triggerType == null) {
            logger.error("Not found simple trigger in database");
            return NOT_FOUND_TRIGGER_TYPE;
        }

        UserTriggerEntity userTrigger = new UserTriggerEntity();
        userTrigger.setUser(user);
        userTrigger.setCurrencyPair(currencyPair);
        userTrigger.setTriggerType(triggerType);

        List<UserTriggerEntity> existingTriggerList = userTriggersRepository.findByUser(user);
        existingTriggerList.forEach(a -> a.setId(null));
        if (existingTriggerList.contains(userTrigger)) {
            logger.debug("User already subscribed to this trigger");
            return ALREADY_SUBSCRIBED;
        }

        userTriggersRepository.save(userTrigger);

        return OK;
    }

    public String getOnce(String currency1Input, String currency2Input) {
        if (currency1Input == null || currency2Input == null) {
            logger.warn("currencies symbol is null");
            return "no data";
        }

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(currency1Input, currency2Input);

        if (currencyPair == null) return "no data";

        return currentCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameUser() +
                currencyPair.getCurrency2().getCurrencyNameUser()).toString();
    }

    public String getAllCurrenciesList() {

        Set<CurrencyEntity> allCurrenciesList = currencyService.getAllCurrencies();

        return allCurrenciesList.stream()
                .map(a -> a.getCurrencyFullName() + ": " + a.getCurrencyNameUser())
                .collect(Collectors.joining("\n"));
    }
}
