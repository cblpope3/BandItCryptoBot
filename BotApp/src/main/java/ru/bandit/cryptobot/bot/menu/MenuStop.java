package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle stop command.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuStop extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuStop(Menu01Main parent, TriggersService triggersService) {
        super(parent);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "stop";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            triggersService.unsubscribeAll(userDTO);
            return "Все подписки успешно удалены";
        } catch (CommonBotAppException e) {
            logger.debug("User {} don't have subscriptions", userDTO.getUserId());
            return e.getUserFriendlyMessage();
        }
    }
}