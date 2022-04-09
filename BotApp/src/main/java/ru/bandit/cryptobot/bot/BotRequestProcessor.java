package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.menu.*;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.CurrencyService;
import ru.bandit.cryptobot.services.MetricsService;
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
    TriggersService triggersService;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    MetricsService metricsService;

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

        MenuItem menuItem;

        //choosing menu item
        switch (requestedMenuItem) {
//===============================================
// Endpoints first
//===============================================
            //THIS PIECE OF SHIT MUST BE REFACTORED
            case ALL_CUR:
                if (logger.isTraceEnabled())
                    logger.trace("Got show all currencies command from user #{}", user.getUserId());
                try {
                    return new BotResponse(menuBack.getMarkup(null, null),
                            currencyService.getAllCurrenciesList());
                } catch (CommonBotAppException e) {
                    logger.warn(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null),
                            e.getUserFriendlyMessage());
                }

            case ONCE:
                if (logger.isTraceEnabled())
                    logger.trace("Got 'once' command from user #{}", user.getUserId());
                try {
                    String rates = String.format("%s/%s: %s", query.getParameter1(), query.getParameter2(),
                            triggersService.getOnce(query));
                    if (logger.isTraceEnabled())
                        logger.trace("Sending 'once' command result to user.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            rates);
                } catch (CommonBotAppException e) {
                    logger.warn(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null), e.getUserFriendlyMessage());
                }

            case STOP:
                if (logger.isTraceEnabled())
                    logger.trace("Got stop command from user #{}", user.getUserId());
                try {
                    triggersService.unsubscribeAll(user);
                    logger.trace("Unsubscribed successfully.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Все подписки удалены. Вы можете посмотреть новые в меню \"Операции с валютами\".");
                } catch (CommonBotAppException e) {
                    logger.debug(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null), e.getUserFriendlyMessage());
                }

            case AVERAGE:
            case TARGET_UP:
            case TARGET_DOWN:
            case SIMPLE:
                try {
                    triggersService.subscribe(user, query);
                    logger.trace("successfully created new simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Подписка успешно создана!");
                } catch (CommonBotAppException e) {
                    logger.warn(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null), e.getUserFriendlyMessage());
                }

            case UNSUBSCRIBE:
                if (logger.isTraceEnabled())
                    logger.trace("Got unsubscribe from command from user #{}", user.getUserId());

                try {
                    triggersService.unsubscribe(user, Long.parseLong(query.getParameter1()));
                    if (logger.isDebugEnabled())
                        logger.debug("User #{} successfully unsubscribed.", user.getUserId());
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Вы отписаны от рассылки.");
                } catch (CommonBotAppException e) {
                    logger.debug(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null), e.getUserFriendlyMessage());
                }

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
                if (logger.isTraceEnabled())
                    logger.trace("Got show subscriptions command from user #{}.", user.getUserId());
                try {
                    String subscriptionsList = triggersService.getAllSubscriptionsAsString(user);
                    if (logger.isTraceEnabled())
                        logger.trace("Sending subscriptions list to user #{}.", user.getUserId());
                    return new BotResponse(menuBack.getMarkup(null, null),
                            subscriptionsList);
                } catch (CommonBotAppException e) {
                    logger.debug(e.getMessage());
                    return new BotResponse(menuBack.getMarkup(null, null), e.getUserFriendlyMessage());
                }


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
                metricsService.incrementHelpCommandCounter();
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