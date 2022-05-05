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
 * Class implementing bot menu item that generate 'select trigger type' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuTriggerType extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuTriggerType(MenuOperations parent, CurrencyService currencyService) {
        super(parent, 2);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "trigger_type";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            CurrencyPairEntity foundCurrencyPair = currencyService.getCurrencyPair(queryParams);
            return String.format("Выберите тип триггера для пары валют %s/%s:",
                    foundCurrencyPair.getCurrency1().getCurrencyNameUser(),
                    foundCurrencyPair.getCurrency2().getCurrencyNameUser());
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

        try {
            CurrencyPairEntity foundCurrencyPair = currencyService.getCurrencyPair(queryParams);

            String currencyPairName = String.format("%s/%s", foundCurrencyPair.getCurrency1().getCurrencyNameUser(),
                    foundCurrencyPair.getCurrency2().getCurrencyNameUser());

            List<List<InlineKeyboardButton>> buttonsGrid = List.of(
                    this.makeHugeButton(
                            String.format("Текущий курс %s", currencyPairName),
                            "/once/" + String.join("/", queryParams)),
                    this.makeHugeButton(String.format("Рассылка курса %s", currencyPairName),
                            "/simple/" + String.join("/", queryParams)),
                    this.makeHugeButton(String.format("Рассылка среднего %s", currencyPairName),
                            "/average/" + String.join("/", queryParams)),
                    this.makeHugeButton(String.format("Будильник %s", currencyPairName),
                            "/target/" + String.join("/", queryParams)),
                    this.getBackButton());

            return new InlineKeyboardMarkup(buttonsGrid);
        } catch (CommonBotAppException e) {
            return new InlineKeyboardMarkup(List.of(this.getBackButton()));
        }
    }
}
