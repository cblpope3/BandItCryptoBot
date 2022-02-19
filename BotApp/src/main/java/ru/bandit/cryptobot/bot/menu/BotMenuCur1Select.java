package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuCur1Select implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите первую валюту из списка:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        //TODO get this from database
        InlineKeyboardButton button1 = new InlineKeyboardButton("BTC");
        InlineKeyboardButton button2 = new InlineKeyboardButton("ETH");
        InlineKeyboardButton button3 = new InlineKeyboardButton("USDT");
        InlineKeyboardButton button4 = new InlineKeyboardButton("BNB");
        InlineKeyboardButton button5 = new InlineKeyboardButton("XRP");
        InlineKeyboardButton button6 = new InlineKeyboardButton("ADA");

        button1.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/BTC");
        button2.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/ETH");
        button3.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/USDT");
        button4.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/BNB");
        button5.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/XRP");
        button6.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/ADA");

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2, button3);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button4, button5, button6);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.SUBSCRIPTIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}