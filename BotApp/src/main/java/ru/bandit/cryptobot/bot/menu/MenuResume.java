package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.services.UsersService;

/**
 * Class implementing bot menu item that handle resume command.
 *
 * @see AbstractMenuItem
 */
@Component
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
        //todo method resumeSubscriptions() should throw exception and it must be handled here
        usersService.resumeSubscriptions(userDTO);
        return "Подписки восстановлены.";
    }
}
