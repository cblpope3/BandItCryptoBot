package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.CurrencyService;

/**
 * Class implementing bot menu item that getting available currencies list.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuCurrenciesList extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuCurrenciesList(Menu01Main parent, CurrencyService currencyService) {
        super(parent);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "all_cur";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {

        try {
            return currencyService.getAllCurrenciesList();
        } catch (CommonBotAppException e) {
            return e.getUserFriendlyMessage();
        }
    }
}
