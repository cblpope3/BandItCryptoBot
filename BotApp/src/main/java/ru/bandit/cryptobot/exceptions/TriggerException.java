package ru.bandit.cryptobot.exceptions;

import lombok.Getter;

/**
 * Exception that can be thrown if issues happen while performing some action with user triggers.
 *
 * @see ExceptionCause
 */
@Getter
public class TriggerException extends Exception {

    private final ExceptionCause exceptionCause;

    public TriggerException(String message, ExceptionCause exceptionCause) {
        super(message);
        this.exceptionCause = exceptionCause;
    }

    /**
     * Enum that explains {@link TriggerException} cause.
     */
    public enum ExceptionCause {
        /**
         * No such currency pair in database.
         */
        NO_CURRENCY_PAIR("Нет подходящей пары валют."),
        /**
         * User already have this subscription.
         */
        ALREADY_SUBSCRIBED("Уже есть такая подписка."),
        /**
         * Requested subscription not found in database.
         */
        SUBSCRIPTION_NOT_FOUND("Такой подписки у вас нет."),
        /**
         * User don't have any subscriptions.
         */
        NO_SUBSCRIPTIONS("У вас нет подписок.");

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
