package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.menu.*;
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
    BotMenuShowAll menuSubscriptionsList;
    @Autowired
    BotMenuTriggerType menuTriggerType;
    @Autowired
    BotMenuPeriod menuPeriod;
    @Autowired
    BotMenuDirection menuDirection;
    @Autowired
    BotMenuValue menuValue;
    @Autowired
    BotMenuBack menuBack;

    @Autowired
    BotService botService;

    public BotResponse generateResponse(List<String> query, Long chatId) {

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
        switch (requestedMenuItem) {
//===============================================
// Endpoints first
//===============================================
            case ALL_CUR:
                return new BotResponse(menuBack.getMarkup(null, null),
                        botService.getAllCurrenciesList().toString());
            case ONCE:
                String rates = String.format("%s/%s: %s", query.get(0), query.get(1), botService.getOnce(query.get(0), query.get(1)));
                return new BotResponse(menuBack.getMarkup(null, null),
                        rates);
            case STOP:
                logger.debug("Got stop command from {}", chatId);
                commandStatus = botService.unsubscribeAll(chatId);
                if (commandStatus == BotService.OK) return new BotResponse(menuBack.getMarkup(null, null),
                        "Подписки успешно удалены.");
                else if (commandStatus == BotService.NO_SUBSCRIPTIONS)
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "У вас не было подписок");
                return new BotResponse(menuBack.getMarkup(null, null),
                        errorMessage);
            case SIMPLE:
                commandStatus = botService.createSimple(chatId, String.join("", query));
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Успешно подписались на " + String.join("/", query));
                } else {
                    logger.error("Error while creating simple trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case AVERAGE:
                commandStatus = botService.createAverage(chatId, query);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new average trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Успешно подписались на среднее: " + String.join("/", query));
                } else {
                    logger.error("Error while creating average trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case TARGET:
                commandStatus = botService.createTrigger(chatId, query);
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully created new target trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Успешно создали будильник: " + String.join("/", query));
                } else {
                    logger.error("Error while creating target trigger");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case UNSUBSCRIBE:
                logger.trace("Got unsubscribe from command from {}", chatId);
                commandStatus = botService.unsubscribe(chatId, String.join("", query));
                if (commandStatus == BotService.OK) {
                    logger.trace("successfully unsubscribed");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Отписались от рассылки на " + String.join("/", query));
                } else if (commandStatus == BotService.NOT_FOUND_CHAT) {
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
                } else {
                    logger.error("Error while trying to resume");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }
            case START:
                //TODO user nickname
                logger.trace("Got start command from {}", chatId);
                commandStatus = botService.addNewUser(chatId);
                if (commandStatus == BotService.OK) {
                    logger.debug("successfully added new user");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Приветствуем в нашем боте курсов криптовалют, $Ник пользователя. Ознакомьтесь с возможными командами нашего бота");
                } else if (commandStatus == BotService.ALREADY_IN_CHAT) {
                    logger.trace("user already exist");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            "Ещё раз привет, $Ник_пользователя! Для помощи напишите /help.");
                } else {
                    logger.error("Error when got /start command.");
                    return new BotResponse(menuBack.getMarkup(null, null),
                            errorMessage);
                }

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
            case PERIOD:
                menuItem = menuPeriod;
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
            case SHOW_ALL:
                menuItem = menuSubscriptionsList;
                break;
            case STOP_CONFIRM:
                menuItem = menuStop;
                break;
            case HELP:
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