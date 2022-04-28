package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.CurrencyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class implementing bot menu for selecting first currency in pair.
 *
 * @see AbstractMenuItem
 * @see MenuOperations
 */
@Component
public class MenuCur1Select extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuCur1Select(MenuOperations parent, CurrencyService currencyService) {
        super(parent);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "cur_1_select";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Выберите первую валюту из списка:";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        List<InlineKeyboardButton> currencyButtonList;

        //getting all available currencies from database and mapping them to corresponding buttons.
        try {
            currencyButtonList = currencyService.getAllCurrencies().stream()
                    .map(currency -> makeButton(currency.getCurrencyNameUser(),
                            "cur_2_select/" + currency.getCurrencyNameUser()))
                    .collect(Collectors.toList());
        } catch (CommonBotAppException e) {
            //if no currencies in database
            logger.error(e.getMessage());
            currencyButtonList = Collections.emptyList();
        }

        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        //making keyboard layout. 3 currencies in row. last row of remaining currencies.
        while (true) {
            if (currencyButtonList.size() > 2) buttonsGrid.add(List.of(currencyButtonList.remove(0),
                    currencyButtonList.remove(0),
                    currencyButtonList.remove(0)));
            else {
                buttonsGrid.add(currencyButtonList);
                break;
            }
        }

        buttonsGrid.add(getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}