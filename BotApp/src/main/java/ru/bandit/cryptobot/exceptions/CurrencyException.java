package ru.bandit.cryptobot.exceptions;

import lombok.Getter;

/**
 * Exception that can be thrown if issues happen while performing some action with currencies.
 *
 * @see ExceptionCause
 * @see CommonBotAppException
 */
@Getter
public class CurrencyException extends CommonBotAppException {

    private final ExceptionCause exceptionCause;

    public CurrencyException(String message, ExceptionCause exceptionCause) {
        super(message);
        this.exceptionCause = exceptionCause;
    }

    @Override
    public String getUserFriendlyMessage() {
        return exceptionCause.getMessage();
    }

    /**
     * Enum that explains {@link CurrencyException} cause.
     */
    public enum ExceptionCause {
        /**
         * No such currency in database.
         */
        NO_CURRENCY("Такой валюты нет."),
        /**
         * Not found any currency in database.
         */
        NO_CURRENCIES_FOUND("В базе данных нет валют."),
        /**
         * Requested currency pair consist of same currencies.
         */
        SAME_CURRENCIES_IN_REQUEST("Запрошены одинаковые валюты."),
        /**
         * No currency pair found.
         */
        NO_CURRENCY_PAIR("Нет такой пары валют."),
        /**
         * Wrong input parameter size.
         */
        WRONG_PARAMETERS("Неправильное количество параметров в запросе."),
        /**
         * No currency pair with given currency.
         */
        NOT_FOUND_PAIR_WITH_CURRENCY("Не найдена пара для запрошенной валюты.");

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
