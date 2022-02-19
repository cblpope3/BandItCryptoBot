package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuCur2Select implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите вторую валюту:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        //TODO get this from database
        //TODO except that have chosen in previous menu

        String prevQuery = param.get(0);

        InlineKeyboardButton button1 = new InlineKeyboardButton("RUB");
        InlineKeyboardButton button2 = new InlineKeyboardButton("USD");
        InlineKeyboardButton button3 = new InlineKeyboardButton("EUR");
        InlineKeyboardButton button4 = new InlineKeyboardButton("BTC");
        InlineKeyboardButton button5 = new InlineKeyboardButton("ETH");
        InlineKeyboardButton button6 = new InlineKeyboardButton("USDT");
        InlineKeyboardButton button7 = new InlineKeyboardButton("BNB");
        InlineKeyboardButton button8 = new InlineKeyboardButton("XRP");
        InlineKeyboardButton button9 = new InlineKeyboardButton("ADA");

        button1.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/RUB");
        button2.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/USD");
        button3.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/EUR");
        button4.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/BTC");
        button5.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/ETH");
        button6.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/USDT");
        button7.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/BNB");
        button8.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/XRP");
        button9.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/ADA");


        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2, button3);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button4, button5, button6);
        List<InlineKeyboardButton> keyboardRow3 = List.of(button7, button8, button9);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Главное меню");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, keyboardRow3, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}