package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.List;

@Component
public class BotMenuBack implements MenuItem {

    @Override
    public String getText(UserDTO user, List<String> param) {
        return null;
    }

    @Override
    public InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param) {

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("В главное меню");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
