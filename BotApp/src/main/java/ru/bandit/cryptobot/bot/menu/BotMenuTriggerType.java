package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuTriggerType implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите тип триггера:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        InlineKeyboardButton button1 = new InlineKeyboardButton("Текущий курс");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Рассылка");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Среднее");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Уведомление");

        button1.setCallbackData(MenuItemsEnum.ONCE + "/" + String.join("/", param));
        button2.setCallbackData(MenuItemsEnum.SIMPLE + "/" + String.join("/", param));
        button3.setCallbackData(MenuItemsEnum.PERIOD + "/" + String.join("/", param));
        button4.setCallbackData(MenuItemsEnum.DIRECTION + "/" + String.join("/", param));

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button2);
        List<InlineKeyboardButton> keyboardRow3 = List.of(button3);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.OPERATIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, keyboardRow3, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
