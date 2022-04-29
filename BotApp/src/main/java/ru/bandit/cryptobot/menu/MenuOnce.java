package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that sending requested current currency rate to user.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuOnce extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuOnce(Menu01Main parent, TriggersService triggersService) {
        super(parent, 2);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "once";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            String currentRate = triggersService.getOnce(new CurrencyPairDTO(queryParams.get(0), queryParams.get(1)));
            return String.format("Текущий курс %s.", currentRate);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
