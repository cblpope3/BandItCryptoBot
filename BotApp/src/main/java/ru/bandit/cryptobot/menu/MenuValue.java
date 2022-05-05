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
            double value = Double.parseDouble(queryParams.get(3));

            return String.format("Пара валют: %s/%s.\nТекущий порог срабатывания - %f%% от текущего курса. Что следует сделать?",
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

        double value;
        if (queryParams.get(3) == null) value = 3;
        else value = Double.parseDouble(queryParams.get(3));

        double plusPercent = value + 1;
        double minusPercent = value - 1;
        double plusPercentSmall = value + 0.1;
        double minusPercentSmall = value - 0.1;
        if (minusPercent < 0.1) minusPercent = 0.1;
        if (minusPercentSmall < 0.1) minusPercentSmall = 0.1;

        String currency1 = foundCurrencyPair.getCurrency1().getCurrencyNameUser();
        String currency2 = foundCurrencyPair.getCurrency2().getCurrencyNameUser();
        String triggerType = queryParams.get(2);

        List<InlineKeyboardButton> keyboardRow1 = List.of(
                this.makeAdjustButton("+1%", foundCurrencyPair, queryParams.get(2), plusPercent),
                this.makeAdjustButton("-1%", foundCurrencyPair, queryParams.get(2), minusPercent)
        );

        List<InlineKeyboardButton> keyboardRow2 = List.of(
                this.makeAdjustButton("+0.1%", foundCurrencyPair, queryParams.get(2), plusPercentSmall),
                this.makeAdjustButton("-0.1%", foundCurrencyPair, queryParams.get(2), minusPercentSmall)
        );

        List<InlineKeyboardButton> keyboardRow3 = this.makeHugeButton("Создать будильник",
                triggerType + "/" + currency1 + "/" + currency2 + "/" + value);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, keyboardRow3, this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }

    /**
     * Method that makes adjustment button.
     *
     * @param name         title of button.
     * @param currencyPair previously selected currency pair.
     * @param triggerType  previously selected trigger type.
     * @param value        new button value.
     * @return button.
     */
    private InlineKeyboardButton makeAdjustButton(String name, CurrencyPairEntity currencyPair, String triggerType, double value) {
        return this.makeButton(name, String.format("/value/%s/%s/%s/%f",
                currencyPair.getCurrency1().getCurrencyNameUser(),
                currencyPair.getCurrency2().getCurrencyNameUser(),
                triggerType,
                value));
    }
}
