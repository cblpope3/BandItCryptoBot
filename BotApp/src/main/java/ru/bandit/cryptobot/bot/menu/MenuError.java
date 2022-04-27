package ru.bandit.cryptobot.bot.menu;

import ru.bandit.cryptobot.exceptions.CommonBotAppException;

/**
 * Menu item to display error messages to user.
 */
public class MenuError extends AbstractMenuItem {

    private final CommonBotAppException exception;

    public MenuError(CommonBotAppException exception) {
        //todo decide if better make this spring-controlled
        super(null);
        this.exception = exception;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return this.exception.getUserFriendlyMessage();
    }
}
