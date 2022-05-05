package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle 'unsubscribe' command.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuUnsubscribe extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuUnsubscribe(Menu01Main parent, TriggersService triggersService) {
        super(parent, 1);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "unsubscribe";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {

        try {
            triggersService.unsubscribe(userDTO, Long.parseLong(queryParams.get(0)));
            return "Подписка удалена.";
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
