package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.CurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class implementing bot menu for selecting second currency in pair.
 *
 * @see AbstractMenuItem
 * @see MenuCur1Select
 */
@Component
@SuppressWarnings("unused")
public class MenuCur2Select extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuCur2Select(MenuCur1Select parent, CurrencyService currencyService) {
        super(parent, 1);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "cur_2_select";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            CurrencyEntity foundCurrency = currencyService.getCurrencyBySymbol(queryParams.get(0));
            currencyService.getAllComplimentaryCurrencies(foundCurrency);
        } catch (CommonBotAppException e) {
            logger.warn(e.getMessage());
            return e.getUserFriendlyMessage();
        }
        return String.format("Первая валюта - %s. Выберите вторую валюту:", queryParams.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        String firstCurrencyName = queryParams.get(0);
        CurrencyEntity firstCurrency;
        Set<CurrencyEntity> secondCurrencies;
        try {
            firstCurrency = currencyService.getCurrencyBySymbol(firstCurrencyName);
            secondCurrencies = currencyService.getAllComplimentaryCurrencies(firstCurrency);
        } catch (CommonBotAppException e) {
            logger.warn(e.getMessage());
            return new InlineKeyboardMarkup(List.of(this.getBackButton()));
        }

        //generating buttons list
        List<InlineKeyboardButton> buttonList = secondCurrencies.stream()
                .map(currency ->
                        this.makeButton(
                                currency.getCurrencyNameUser(),
                                "trigger_type/" + firstCurrencyName + "/" + currency.getCurrencyNameUser())
                )
                .collect(Collectors.toList());

        //generating keyboard grid with 3 buttons in row
        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();
        while (true) {
            if (buttonList.size() > 2) buttonsGrid.add(List.of(buttonList.remove(0),
                    buttonList.remove(0),
                    buttonList.remove(0)));
            else {
                buttonsGrid.add(buttonList);
                break;
            }
        }
        buttonsGrid.add(this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}