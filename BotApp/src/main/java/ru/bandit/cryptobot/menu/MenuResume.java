package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.UsersService;

/**
 * Class implementing bot menu item that handle resume command.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuResume extends AbstractMenuItem {

    private final UsersService usersService;

    @Autowired
    protected MenuResume(Menu01Main parent, UsersService usersService) {
        super(parent);
        this.usersService = usersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "resume";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            usersService.resumeSubscriptions(userDTO);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
        return "Подписки восстановлены.";
    }
}
