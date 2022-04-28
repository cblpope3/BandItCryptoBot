package ru.bandit.cryptobot.exceptions;

import lombok.Getter;

/**
 * Exception that can be thrown if issues happen while performing some action with user data.
 *
 * @see ExceptionCause
 * @see CommonBotAppException
 */
@Getter
public class UserException extends CommonBotAppException {

    private final ExceptionCause exceptionCause;

    public UserException(String message, ExceptionCause exceptionCause) {
        super(message);
        this.exceptionCause = exceptionCause;
    }

    @Override
    public String getUserFriendlyMessage() {
        return this.exceptionCause.getMessage();
    }

    /**
     * Enum that explains {@link UserException} cause.
     */
    public enum ExceptionCause {
        /**
         * Subscriptions already paused.
         */
        ALREADY_PAUSED("Подписки уже были на паузе."),
        /**
         * Subscriptions already resumed.
         */
        ALREADY_RESUMED("Подписки и так работают.");

        private final String message;

        ExceptionCause(String message) {
            this.message = message;
        }

        /**
         * Get user-friendly exception cause.
         *
         * @return exception cause as {@link String}.
         */
        public String getMessage() {
            return message;
        }
    }
}
