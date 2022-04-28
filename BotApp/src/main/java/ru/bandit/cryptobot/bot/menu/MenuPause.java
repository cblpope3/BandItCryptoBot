package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.UsersService;

/**
 * Class implementing bot menu item that handle pause command.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuPause extends AbstractMenuItem {

    private final UsersService usersService;

    @Autowired
    protected MenuPause(Menu01Main parent, UsersService usersService) {
        super(parent);
        this.usersService = usersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "pause";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            usersService.pauseSubscriptions(userDTO);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
        return "Подписки на паузе.";
    }
}
