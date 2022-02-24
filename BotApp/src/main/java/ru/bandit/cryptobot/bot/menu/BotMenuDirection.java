package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuDirection implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите искомое направление изменения:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        InlineKeyboardButton button1 = new InlineKeyboardButton("Курс выше порога");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Курс ниже порога");

        button1.setCallbackData(MenuItemsEnum.VALUE + "/" + String.join("/", param) + "/UP");
        button2.setCallbackData(MenuItemsEnum.VALUE + "/" + String.join("/", param) + "/DOWN");


        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Главное меню");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
