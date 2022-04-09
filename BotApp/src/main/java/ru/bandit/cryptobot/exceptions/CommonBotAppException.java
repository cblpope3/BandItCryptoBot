package ru.bandit.cryptobot.exceptions;

/**
 * Abstract class that is template for exceptions. These exceptions forming user-friendly error messages.
 */
public abstract class CommonBotAppException extends Exception {

    protected CommonBotAppException(String message) {
        super(message);
    }

    /**
     * Get user-friendly text of error message.
     *
     * @return error message that can be delivered directly to user.
     */
    public abstract String getUserFriendlyMessage();
}
