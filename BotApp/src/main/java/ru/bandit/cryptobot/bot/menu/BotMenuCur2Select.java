package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.repositories.CurrencyPairRepository;
import ru.bandit.cryptobot.repositories.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BotMenuCur2Select implements MenuItem {

    @Autowired
    CurrencyPairRepository currencyPairRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите вторую валюту:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        //BETTER REFACTOR THIS METHOD. TOO COMPLEX

        String prevQuery = param.get(0);

        //collecting all available currency pairs with previous entered currency
        CurrencyEntity prevCurrency = currencyRepository.findByCurrencyNameUser(param.get(0));
        List<CurrencyPairEntity> currencyPairEntityList = new ArrayList<>();
        currencyPairEntityList.addAll(currencyPairRepository.findByCurrency1(prevCurrency));
        currencyPairEntityList.addAll(currencyPairRepository.findByCurrency2(prevCurrency));

        //filtering all available currencies that can be combined with previous entered currency
        List<String> currencyLabelsList = currencyPairEntityList.stream()
                .flatMap(a -> Stream.of(a.getCurrency1(), a.getCurrency2()))
                .map(CurrencyEntity::getCurrencyNameUser)
                .filter(currencyNameUser -> !currencyNameUser.equals(prevQuery))
                .collect(Collectors.toList());

        //generating buttons list
        List<InlineKeyboardButton> currencyButtonList = new ArrayList<>();
        for (String buttonLabel : currencyLabelsList) {
            InlineKeyboardButton button = new InlineKeyboardButton(buttonLabel);
            button.setCallbackData(MenuItemsEnum.TRIGGER_TYPE + "/" + prevQuery + "/" + buttonLabel);
            currencyButtonList.add(button);
        }

        //generating keyboard grid
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
        buttonBack.setCallbackData(MenuItemsEnum.SELECT_1_CUR.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);
        buttonsGrid.add(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}