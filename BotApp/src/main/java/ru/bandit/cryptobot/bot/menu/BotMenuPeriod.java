package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class BotMenuPeriod implements MenuItem {

    @Override
    public String getText(Long userId, List<String> param) {
        return "Текущий период усреднения - " + param.get(2) + " минут. Что следует сделать?";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {


        if (param.size() < 3) {
            param.add("10");
        }

        int period = Integer.parseInt(param.get(2));
        int nextPeriod = period + 1;
        int prevPeriod = period - 1;

        if (prevPeriod < 1) prevPeriod = 1;

        String currency1 = param.get(0);
        String currency2 = param.get(1);

        InlineKeyboardButton button1 = new InlineKeyboardButton("+");
        InlineKeyboardButton button2 = new InlineKeyboardButton("-");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Создать");

        button1.setCallbackData(MenuItemsEnum.PERIOD + "/" + currency1 + "/" + currency2 + "/" + nextPeriod);
        button2.setCallbackData(MenuItemsEnum.PERIOD + "/" + currency1 + "/" + currency2 + "/" + prevPeriod);
        button3.setCallbackData(MenuItemsEnum.AVERAGE + "/" + String.join("/", param));

        List<InlineKeyboardButton> keyboardRow1 = List.of(button1, button2);
        List<InlineKeyboardButton> keyboardRow2 = List.of(button3);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Главное меню");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
