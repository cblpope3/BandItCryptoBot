package ru.bandit.cryptobot.exceptions;

import lombok.Getter;

/**
 * Exception that can be thrown if user query has some issues.
 *
 * @see ExceptionCause
 * @see CommonBotAppException
 */
@Getter
public class QueryException extends CommonBotAppException {

    private final ExceptionCause exceptionCause;

    public QueryException(String message, ExceptionCause exceptionCause) {
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
         * Unsupported symbols in query.
         */
        UNSUPPORTED_SYMBOLS("В запросе есть неподдерживаемые символы."),
        /**
         * Entered command not recognized.
         */
        COMMAND_NOT_FOUND("Введена неподдерживаемая команда. Чтобы увидеть список доступных команд наберите /help."),
        /**
         * This command requires other parameters number.
         */
        WRONG_PARAMETERS_NUMBER("Неверное количество параметров для данной команды.");

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
