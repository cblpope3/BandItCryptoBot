package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that getting existing user subscriptions list.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuMySubscriptions extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuMySubscriptions(MenuOperations parent, TriggersService triggersService) {
        super(parent);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "my_subscriptions";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            return triggersService.getAllSubscriptionsAsString(userDTO);
        } catch (CommonBotAppException e) {
            logger.debug("User #{} don't have any subscriptions.", userDTO.getUserId());
            return e.getUserFriendlyMessage();
        }
    }
}
