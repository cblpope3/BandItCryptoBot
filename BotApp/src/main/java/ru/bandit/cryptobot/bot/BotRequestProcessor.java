package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.menu.*;
import ru.bandit.cryptobot.entities.MetricsEntity;
import ru.bandit.cryptobot.repositories.MetricsRepository;
import ru.bandit.cryptobot.services.BotService;

import java.util.List;

@Service
public class BotRequestProcessor {

    Logger logger = LoggerFactory.getLogger(BotRequestProcessor.class);

    @Autowired
    BotMenuMain menuMain;
    @Autowired
    BotMenuOperations menuSubscriptions;
    @Autowired
    BotMenuCur1Select menuCryptocurrencySelect;
    @Autowired
    BotMenuCur2Select menuCurrencySelect;
    @Autowired
    BotMenuHelp menuHelp;
    @Autowired
    BotMenuStop menuStop;
    @Autowired
    BotMenuUnsubscribeSelect menuRemoveSubscription;
    @Autowired
    BotMenuTriggerType menuTriggerType;
    @Autowired
    BotMenuDirection menuDirection;
    @Autowired
    BotMenuValue menuValue;
    @Autowired
    BotMenuBack menuBack;

    @Autowired
    MetricsRepository metricsRepository;

    @Autowired
    BotService botService;

    public BotResponse generateResponse(List<String> query, Long chatId, String username) {

        MenuItemsEnum requestedMenuItem;
        String command = query.remove(0);

        try {
            requestedMenuItem = MenuItemsEnum.valueOf(command);
        } catch (IllegalArgumentException e) {
            logger.warn("Command {} is not recognized.", command);
            return new BotResponse(null, "Неизвестная команда. Напишите /help, чтобы увидеть список команд.");
        }

        short commandStatus;
        MenuItem menuItem;

        //choosing menu item
        String errorMessage = "Произошла ошибка. Для помощи напишите /help.";
        String userNotFound = "Сначала наберите /start.";
        String userNotFoundLog = "User not found.";
        switch (requestedMenuItem) {
//===============================================
// Endpoints first
//===============================================
            //THIS PIECE OF SHIT MUST BE REFACTORED
            case ALL_CUR:
                return new BotResponse(menuBack.getMarkup(null, null),
                        botService.getAllCurrenciesList());
            //todo once trigger is not implemented and tested
            case ONCE:
                String rates = String.format("%s/%s: %s", query.get(0), query.get(1), botService.getOnce(query.get(0), query.get(1)));
                return new BotResponse(menuBack.getMarkup(null, null),
                        rates);
            case STOP:
                logger.debug("Got stop command from {}", chatId);
                commandStatus = botService.unsubscribeAll(chatId);
                if (commandStatus == BotService.OK) {
                    logger.trace("Unsubscribed successfully.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Подписки успешно удалены.");
                } else if (commandStatus == BotService.NO_SUBSCRIPTIONS) {
                    logger.trace("No subscriptions found.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас не было подписок.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while unsubscribing.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case SIMPLE:
                commandStatus = botService.createSimple(chatId, query);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Подписка создана успешно!");
                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
                    logger.trace("not found currency");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Выбранный набор валют недоступен.");
                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
                    logger.trace("not valid request parameters");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Неверные параметры запроса.");
                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
                    logger.trace("already have this subscription");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас уже есть такая подписка.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while creating simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case AVERAGE:
                commandStatus = botService.createAverage(chatId, query);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Подписка создана успешно!");
                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
                    logger.trace("not found currency");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Выбранный набор валют недоступен.");
                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
                    logger.trace("not valid request parameters");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Неверные параметры запроса.");
                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
                    logger.trace("already have this subscription");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас уже есть такая подписка.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while creating simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case TARGET:
                commandStatus = botService.createTarget(chatId, query);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Подписка создана успешно!");
                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
                    logger.trace("not found currency");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Выбранный набор валют недоступен.");
                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
                    logger.trace("not valid request parameters");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Неверные параметры запроса.");
                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
                    logger.trace("already have this subscription");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас уже есть такая подписка.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while creating simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case UNSUBSCRIBE:
                logger.trace("Got unsubscribe from command from {}", chatId);
                commandStatus = botService.unsubscribe(chatId, Long.parseLong(query.remove(0)));
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully unsubscribed");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Вы отписаны от рассылки.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.warn("not found user");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Сначала наберите команду /start");
                } else if (commandStatus == BotService.NOT_FOUND_SUBSCRIPTION) {
                    logger.trace("subscription not found");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас нет такой подписки.");
                } else {
                    logger.error("Error while unsubscribing");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case PAUSE:
                logger.trace("Got pause command from {}", chatId);
                commandStatus = botService.pauseUser(chatId);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully paused");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Ваши рассылки на паузе.");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while trying to pause");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case RESUME:
                logger.trace("Got resume command from {}", chatId);
                commandStatus = botService.resumeUser(chatId);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully resumed");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Ваши рассылки восстановлены!");
                } else if (commandStatus == BotService.NOT_FOUND_USER) {
                    logger.trace(userNotFoundLog);
                    return new BotResponse(menuBack.getMarkup(null, null),
                            userNotFound);
                } else {
                    logger.error("Error while trying to resume");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case START:
                logger.trace("Got start command from {}", chatId);
                commandStatus = botService.addNewUser(chatId, username);
                if (commandStatus == BotService.OK) {
                    logger.debug("successfully added new user");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            String.format("Приветствуем в нашем боте курсов криптовалют, %s. Ознакомьтесь с возможными командами нашего бота", username));
                } else if (commandStatus == BotService.ALREADY_IN_CHAT) {
                    logger.trace("user already exist");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            String.format("Ещё раз привет, %s! Для помощи напишите /help.", username));
                } else {
                    logger.error("Error when got /start command.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case SHOW_ALL:
                logger.trace("Got show subscriptions command from {}.", chatId);
                String subscriptionsList = botService.getAllSubscriptions(chatId);
                if (subscriptionsList == null || subscriptionsList.isEmpty()) {
                    logger.trace("Subscriptions list is empty");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас нет подписок");
                } else return new BotResponse(menuBack.getMarkup(null, null),
                        subscriptionsList);

//===============================================

//===============================================
            case MAIN:
                menuItem = menuMain;
                break;
            case OPERATIONS:
                menuItem = menuSubscriptions;
                break;
            case SELECT_1_CUR:
                menuItem = menuCryptocurrencySelect;
                break;
            case SELECT_2_CUR:
                menuItem = menuCurrencySelect;
                break;
            case TRIGGER_TYPE:
                menuItem = menuTriggerType;
                break;
            case DIRECTION:
                menuItem = menuDirection;
                break;
            case VALUE:
                menuItem = menuValue;
                break;
            case UNSUBSCRIBE_SELECT:
                menuItem = menuRemoveSubscription;
                break;

            case STOP_CONFIRM:
                menuItem = menuStop;
                break;
            case HELP:
                MetricsEntity metrics = metricsRepository.findById(1L);
                if (metrics == null) metrics = new MetricsEntity();
                metrics.setHelpCount(metrics.getHelpCount() + 1);
                metricsRepository.save(metrics);
                logger.trace("Got help command from {}", chatId);
                menuItem = menuHelp;
                break;
            default:
                logger.error("Got command that doesn't have handler.");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Произошла ошибка!");
        }

        return new BotResponse(menuItem.getMarkup(chatId, query), menuItem.getText(chatId, query));
    }
}