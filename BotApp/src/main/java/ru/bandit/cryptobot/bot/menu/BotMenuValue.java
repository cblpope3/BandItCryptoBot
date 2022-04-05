package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.List;

@Component
public class BotMenuValue implements MenuItem {

    @Override
    public String getText(UserDTO user, List<String> param) {
        return "Текущий порог срабатывания - " + param.get(3) + "% от текущего курса. Что следует сделать?";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param) {

        if (param.size() < 4) param.add("10");

        int value = Integer.parseInt(param.get(3));
        int nextValue = value + 1;
        int prevValue = value - 1;
        if (prevValue < 1) prevValue = 1;
        String currency1 = param.get(0);
        String currency2 = param.get(1);
        String triggerType = param.get(2);

        InlineKeyboardButton button1 = new InlineKeyboardButton("+");
        InlineKeyboardButton button2 = new InlineKeyboardButton("-");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Создать");

        button1.setCallbackData(MenuItemsEnum.VALUE + "/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + nextValue);
        button2.setCallbackData(MenuItemsEnum.VALUE + "/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + prevValue);
        button3.setCallbackData(MenuItemsEnum.TARGET + "/" + String.join("/", param));

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button3);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Главное меню");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
