package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.menu.*;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.MetricsEntity;
import ru.bandit.cryptobot.repositories.MetricsRepository;
import ru.bandit.cryptobot.services.CurrencyService;
import ru.bandit.cryptobot.services.TriggersService;
import ru.bandit.cryptobot.services.UsersService;

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
    TriggersService triggersService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    UsersService usersService;

    public BotResponse generateResponse(QueryDTO query, UserDTO user) {

        MenuItemsEnum requestedMenuItem;
        String command = query.getCommand();

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
                        currencyService.getAllCurrenciesList());
            //todo once trigger is not implemented and tested
            case ONCE:
                String rates = String.format("%s/%s: %s", query.getParameter1(), query.getParameter2(),
                        triggersService.getOnce(query));
                return new BotResponse(menuBack.getMarkup(null, null),
                        rates);
            case STOP:
                logger.debug("Got stop command from #{}", user.getUserId());
                triggersService.unsubscribeAll(user);
                //todo catch exceptions
                logger.trace("Unsubscribed successfully.");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Все подписки удалены. Вы можете посмотреть новые в меню \"Операции с валютами\".");
//                } else if (commandStatus == BotService.NO_SUBSCRIPTIONS) {
//                    logger.trace("No subscriptions found.");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас не было подписок.");
//                } else if (commandStatus == BotService.NOT_FOUND_USER) {
//                    logger.trace(userNotFoundLog);
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            userNotFound);
//                } else {
//                    logger.error("Error while unsubscribing.");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            errorMessage);
//                }
            case AVERAGE:
            case TARGET_UP:
            case TARGET_DOWN:
            case SIMPLE:
                triggersService.subscribe(user, query);
                //todo catch exceptions
                logger.trace("successfully created new simple trigger");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Подписка успешно создана!");
//                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
//                    logger.trace("not found currency");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Выбранный набор валют недоступен.");
//                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
//                    logger.trace("not valid request parameters");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Неверные параметры запроса.");
//                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
//                    logger.trace("already have this subscription");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас уже есть такая подписка.");
//                } else if (commandStatus == BotService.NOT_FOUND_USER) {
//                    logger.trace(userNotFoundLog);
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            userNotFound);
//                } else {
//                    logger.error("Error while creating simple trigger");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            errorMessage);
//                }
//            case AVERAGE:
//                triggersService.subscribe(user, query);
//                //todo catch exceptions
//                logger.trace("successfully created new average trigger");
//                return new BotResponse(menuBack.getMarkup(null, null),
//                        "Подписка успешно создана!");
//                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
//                    logger.trace("not found currency");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Выбранный набор валют недоступен.");
//                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
//                    logger.trace("not valid request parameters");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Неверные параметры запроса.");
//                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
//                    logger.trace("already have this subscription");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас уже есть такая подписка.");
//                } else if (commandStatus == BotService.NOT_FOUND_USER) {
//                    logger.trace(userNotFoundLog);
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            userNotFound);
//                } else {
//                    logger.error("Error while creating simple trigger");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            errorMessage);
//                }
            case TARGET:
                triggersService.subscribe(user, query);
                //todo catch exceptions

                logger.trace("successfully created new target trigger");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Подписка успешно создана!");
//                } else if (commandStatus == BotService.NOT_FOUND_CURRENCY) {
//                    logger.trace("not found currency");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Выбранный набор валют недоступен.");
//                } else if (commandStatus == BotService.NOT_VALID_PARAMETER) {
//                    logger.trace("not valid request parameters");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Неверные параметры запроса.");
//                } else if (commandStatus == BotService.ALREADY_SUBSCRIBED) {
//                    logger.trace("already have this subscription");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас уже есть такая подписка.");
//                } else if (commandStatus == BotService.NOT_FOUND_USER) {
//                    logger.trace(userNotFoundLog);
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            userNotFound);
//                } else {
//                    logger.error("Error while creating simple trigger");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            errorMessage);
//                }
            case UNSUBSCRIBE:
                logger.trace("Got unsubscribe from command from user #{}", user.getUserId());
                triggersService.unsubscribe(user, Long.parseLong(query.getParameter1()));
                //todo catch exceptions
                logger.trace("successfully unsubscribed");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Вы отписаны от рассылки.");
//                } else if (commandStatus == BotService.NOT_FOUND_USER) {
//                    logger.warn("not found user");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "Сначала наберите команду /start");
//                } else if (commandStatus == BotService.NOT_FOUND_SUBSCRIPTION) {
//                    logger.trace("subscription not found");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас нет такой подписки.");
//                } else {
//                    logger.error("Error while unsubscribing");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            errorMessage);
//                }
            case PAUSE:
                logger.trace("Got pause command from user #{}", user.getUserId());
                usersService.pauseSubscriptions(user);
                logger.trace("successfully paused");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Все подписки остановлены. Вы можете посмотреть свои подписки и возобновить их в любой момент.");
//todo this code must be used in exception
//                } else {
//                    logger.trace("no subscriptions");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас нет подписок.");
//                }
            case RESUME:
                logger.trace("Got resume command from user #{}", user.getUserId());
                usersService.resumeSubscriptions(user);
                logger.trace("successfully resumed");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Все подписки восстановлены!");
//todo this code must be used in exception
//                } else {
//                    logger.trace("no subscriptions");
//                    return new BotResponse(menuBack.getMarkup(null, null),
//                            "У вас нет подписок.");
//                }
            case START:
                logger.trace("Got start command from user #{}", user.getUserId());
                boolean startStatus = usersService.startUser(user);
                if (startStatus) {
                    logger.debug("successfully added new user");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            String.format("Приветствуем в нашем боте курсов криптовалют, %s. Ознакомьтесь с возможными командами нашего бота",
                                    user.getFirstName()));
                } else {
                    logger.trace("user already exist");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            String.format("Ещё раз привет, %s! Для помощи напишите /help.", user.getFirstName()));
                }
            case SHOW_ALL:
                logger.trace("Got show subscriptions command from user #{}.", user.getUserId());
                String subscriptionsList = triggersService.getAllSubscriptionsAsString(user);
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
                return new BotResponse(menuItem.getMarkup(user, null), menuItem.getText(user, null));
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
                logger.trace("Got help command from user #{}", user.getUserId());
                menuItem = menuHelp;
                return new BotResponse(menuItem.getMarkup(user, null), menuItem.getText(user, null));
            default:
                logger.error("Got command that doesn't have handler.");
                return new BotResponse(menuBack.getMarkup(null, null),
                        "Произошла ошибка!");
        }


        //todo watch this
        return null;
        //return new BotResponse(menuItem.getMarkup(user, query), menuItem.getText(user, query));
    }
}