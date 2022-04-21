package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that sending requested current currency rate to user.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuOnce extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuOnce(Menu01Main parent, TriggersService triggersService) {
        super(parent);
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
            return triggersService.getOnce(queryDTO);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
