package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuHelp implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        //TODO
        return "Тут пишем текст хэлпа";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {
        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}