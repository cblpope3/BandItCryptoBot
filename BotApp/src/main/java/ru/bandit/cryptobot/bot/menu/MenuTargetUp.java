package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle 'create target_up trigger' command.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuTargetUp extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuTargetUp(Menu01Main parent, TriggersService triggersService) {
        super(parent);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "target_up";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            triggersService.subscribe(userDTO, queryDTO);
            return "Будильник создан.";
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
