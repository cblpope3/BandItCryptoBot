package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bandit.cryptobot.bot.menu.*;
import ru.bandit.cryptobot.services.BotService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BotRequestProcessor {

    Logger logger = LoggerFactory.getLogger(BotRequestProcessor.class);

    @Autowired
    BotMenuMain menuMain;
    @Autowired
    BotMenuSubscriptions menuSubscriptions;
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
    BotService botService;

    public String processTextRequest(Message message) {
        long chatId = message.getChatId();

        List<String> separatedMessage = Arrays.asList(message.getText().split(" "));

        logger.trace("Received command list: {}", separatedMessage);
        int commandStatus;
        switch (separatedMessage.get(0)) {
            case "/start":
                logger.debug("Got start command from {}", chatId);
                commandStatus = botService.appendChatNewcomer(chatId);
                if (commandStatus == BotService.OK) return "Привет! Я бот от команды BandIt! Для помощи напиши /help";
                else if (commandStatus == BotService.ALREADY_IN_CHAT) return "Ещё раз привет!";
                break;
            case "/stop":
                logger.debug("Got stop command from {}", chatId);
                commandStatus = botService.removeChatMember(chatId);
                if (commandStatus == BotService.OK) return "До встречи! Пиши /start, если соскучишься...";
                else if (commandStatus == BotService.NOT_FOUND_CHAT) return "Уже прощались.";
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
                commandStatus = botService.subscribe(chatId, separatedMessage.get(1));
                if (commandStatus == BotService.OK) return "Подписались на " + separatedMessage.get(1);
                else if (commandStatus == BotService.NOT_FOUND_CHAT) return "Сначала наберите команду /start";
                else if (commandStatus == BotService.ALREADY_SUBSCRIBED) return "Уже подписаны на эту рассылку";
                else if (commandStatus == BotService.NOT_FOUND_CURRENCY)
                    return "Такой валюты я не знаю: " + separatedMessage.get(1);
                break;
            case "/unsubscribe":
                logger.debug("Got unsubscribe from {} command from {}", separatedMessage.get(0), chatId);
                commandStatus = botService.unsubscribe(chatId, separatedMessage.get(1));
                if (commandStatus == BotService.OK) return "Отписались от рассылки на " + separatedMessage.get(1);
                else if (commandStatus == BotService.NOT_FOUND_CHAT) return "Сначала наберите команду /start";
                else if (commandStatus == BotService.NOT_FOUND_SUBSCRIPTION)
                    return "Вы ещё не подписались на " + separatedMessage.get(1);
                break;
            default:
                return "Неизвестная команда";
        }

        logger.error("Error in switch-case. Processing message from {} is: {}. \nSeparated message: {}",
                message.getChatId(), message.getText(), separatedMessage);
        return "Что-то пошло не так";
    }

    public EditMessageText processCallbackRequest(CallbackQuery request) {
        //Some button had been pressed. Processing request:

        if (request.getData() == null) {
            logger.error("Request is null: {}", request);
            return null;
        }

        System.out.println("got new callback request. call_data: " + request.getData());

        //extracting data from request body
        List<String> requestQueryList = new ArrayList<>(List.of(request.getData().toUpperCase().split("/")));
        MenuItemsEnum requestedMenuItem = MenuItemsEnum.valueOf(requestQueryList.get(0));
        requestQueryList.remove(0);

        Long chatId = request.getMessage().getChatId();

        //FIXME remove '= new BotMenuMain()'
        MenuItem menuItem = new BotMenuMain();

        //choosing menu item
        switch (requestedMenuItem) {
            case STOP:
                //TODO here is stop handler
                botService.unsubscribeAll(chatId);
                menuItem = menuMain;
                break;
            case SUBSCRIBE:
                //TODO here is Subscribe handler
                botService.subscribe(chatId, String.join("", requestQueryList));
                menuItem = menuMain;
                break;
            case AVERAGE:
                //TODO here is average handler
                break;
            case TRIGGER:
                //TODO here is trigger handler
                break;
            case UNSUBSCRIBE:
                //TODO here is unsubscribe handler
                botService.unsubscribe(chatId, String.join("", requestQueryList));
                menuItem = menuMain;
                break;
            case PAUSE:
                //TODO here is pause handler
                break;
            case RESUME:
                //TODO here is resume handler
                break;
            case MAIN:
                menuItem = menuMain;
                break;
            case SUBSCRIPTIONS:
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
                menuItem = menuHelp;
                break;
            default:
                logger.error("Unknown user request");
                break;
        }

        EditMessageText responseMessage = new EditMessageText();

        responseMessage.setReplyMarkup(menuItem.getMarkup(chatId, requestQueryList));
        responseMessage.setText(menuItem.getText(chatId, requestQueryList));
        responseMessage.setChatId(chatId.toString());

        return responseMessage;
    }
}