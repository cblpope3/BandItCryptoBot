package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.repositories.CurrencyPairRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BotMenuCur1Select implements MenuItem {

    @Autowired
    CurrencyPairRepository currencyPairRepository;

    @Override
    public String getText(UserDTO user, List<String> param) {
        return "Выберите первую валюту из списка:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param) {

        Set<InlineKeyboardButton> currencyButtonSet = new HashSet<>();

        //fixme this might work wrong. looking only in currency1 position
        for (CurrencyPairEntity currencyPair : currencyPairRepository.findAll()) {
            if (currencyPair.getCurrency1().isCrypto()) {
                InlineKeyboardButton button = new InlineKeyboardButton(currencyPair.getCurrency1().getCurrencyNameUser());
                button.setCallbackData(MenuItemsEnum.SELECT_2_CUR + "/" + currencyPair.getCurrency1().getCurrencyNameUser());
                currencyButtonSet.add(button);
            }
        }

        List<InlineKeyboardButton> currencyButtonList = new ArrayList<>(currencyButtonSet);

        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        while (true) {
            if (currencyButtonList.size() > 2) buttonsGrid.add(List.of(currencyButtonList.remove(0),
                    currencyButtonList.remove(0),
                    currencyButtonList.remove(0)));
            else {
                buttonsGrid.add(currencyButtonList);
                break;
            }
        }

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.OPERATIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);
        buttonsGrid.add(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}