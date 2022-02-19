package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuMain implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите желаемое действие:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        InlineKeyboardButton button1 = new InlineKeyboardButton("Управление подписками");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Пауза");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Возобновить подписки");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Выключить бота");
        InlineKeyboardButton button5 = new InlineKeyboardButton("Помощь");

        button1.setCallbackData(MenuItemsEnum.SUBSCRIPTIONS.toString());
        button2.setCallbackData(MenuItemsEnum.PAUSE.toString());
        button3.setCallbackData(MenuItemsEnum.RESUME.toString());
        button4.setCallbackData(MenuItemsEnum.STOP_CONFIRM.toString());
        button5.setCallbackData(MenuItemsEnum.HELP.toString());

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button2);
        List<InlineKeyboardButton> keyboardRow3 = List.of(button3);
        List<InlineKeyboardButton> keyboardRow4 = List.of(button4);
        List<InlineKeyboardButton> keyboardRow5 = List.of(button5);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4, keyboardRow5);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}