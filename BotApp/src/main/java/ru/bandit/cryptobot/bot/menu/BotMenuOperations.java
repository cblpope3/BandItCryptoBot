package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuOperations implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Что хотите сделать с валютами?";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        InlineKeyboardButton button1 = new InlineKeyboardButton("Подписаться");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Отписаться");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Список");

        button1.setCallbackData(MenuItemsEnum.SELECT_1_CUR.toString());
        button2.setCallbackData(MenuItemsEnum.UNSUBSCRIBE_SELECT.toString());
        button3.setCallbackData(MenuItemsEnum.SHOW_ALL.toString());

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2, button3);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}