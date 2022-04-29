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
 * Class implementing bot menu item that generate 'select target trigger type' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuTarget extends AbstractMenuItem {

    private final CurrencyService currencyService;

    @Autowired
    protected MenuTarget(MenuOperations parent, CurrencyService currencyService) {
        super(parent, 2);
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "target";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            CurrencyPairEntity foundPair = currencyService.getCurrencyPair(queryParams);
            return String.format("Выберите тип будильника для валют %s/%s:",
                    foundPair.getCurrency1().getCurrencyNameUser(),
                    foundPair.getCurrency2().getCurrencyNameUser());
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
            CurrencyPairEntity foundPair = currencyService.getCurrencyPair(queryParams);
            String foundPairName = String.format("%s/%s", foundPair.getCurrency1().getCurrencyNameUser(),
                    foundPair.getCurrency2().getCurrencyNameUser());

            List<List<InlineKeyboardButton>> buttonsGrid = List.of(
                    this.makeHugeButton(String.format("Курс %s выше порога", foundPairName),
                            "/value/" + String.join("/", queryParams) + "/target_up/10"),
                    this.makeHugeButton(String.format("Курс %s ниже порога", foundPairName),
                            "/value/" + String.join("/", queryParams) + "/target_down/10"),
                    this.getBackButton());

            return new InlineKeyboardMarkup(buttonsGrid);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return new InlineKeyboardMarkup(List.of(this.getBackButton()));
        }
    }
}
