package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.List;

@Component
public class BotMenuStop implements MenuItem {

    @Override
    public String getText(UserDTO user, List<String> param) {
        return "Вы действительно хотите удалить все подписки? Действие необратимо.";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param) {


        InlineKeyboardButton button1 = new InlineKeyboardButton("Да");
        button1.setCallbackData(MenuItemsEnum.STOP.toString());

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}