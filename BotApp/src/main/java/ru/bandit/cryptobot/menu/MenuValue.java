package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.CurrencyService;

import java.util.List;

/**
 * Class implementing bot menu item that generate 'enter target trigger value' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuValue extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuValue(MenuOperations parent, CurrencyService currencyService) {
        super(parent, 4);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "value";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {

        try {
            CurrencyPairEntity foundCurrencyPair = currencyService.getCurrencyPair(queryParams.get(0), queryParams.get(1));
            int value = Integer.parseInt(queryParams.get(3));

            return String.format("Пара валют: %s/%s.\nТекущий порог срабатывания - %d%% от текущего курса. Что следует сделать?",
                    foundCurrencyPair.getCurrency1().getCurrencyNameUser(),
                    foundCurrencyPair.getCurrency2().getCurrencyNameUser(),
                    value);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        CurrencyPairEntity foundCurrencyPair;
        try {
            foundCurrencyPair = currencyService.getCurrencyPair(queryParams.get(0), queryParams.get(1));
        } catch (CommonBotAppException e) {
            //if exception happened - returning only back button
            return new InlineKeyboardMarkup(List.of(this.getBackButton()));
        }

        int value;
        if (queryParams.get(3) == null) value = 10;
        else value = Integer.parseInt(queryParams.get(3));

        int nextValue = value + 1;
        int prevValue = value - 1;
        if (prevValue < 1) prevValue = 1;

        String currency1 = foundCurrencyPair.getCurrency1().getCurrencyNameUser();
        String currency2 = foundCurrencyPair.getCurrency2().getCurrencyNameUser();
        String triggerType = queryParams.get(2);

        List<InlineKeyboardButton> keyboardRow1 = List.of(
                this.makeButton("+", "/value/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + nextValue),
                this.makeButton("-", "/value/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + prevValue)
        );

        List<InlineKeyboardButton> keyboardRow2 = this.makeHugeButton("Создать будильник",
                triggerType + "/" + currency1 + "/" + currency2 + "/" + value);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
