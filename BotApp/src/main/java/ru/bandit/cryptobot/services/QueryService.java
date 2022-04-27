package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.QueryException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Split incoming {@link String} query to {@link List} of commands and make basic validations.
     *
     * @param query incoming query
     * @return split query.
     * @throws CommonBotAppException in case of problems during query validation process.
     */
    public List<String> separateQuery(String query) throws CommonBotAppException {

        //check if query contains unexpected symbols
        this.checkQueryIllegalSymbols(query);

        //split query by special letters
        List<String> splitQuery = Arrays.stream(query.split("[/,-]"))
                .filter(Objects::nonNull)
                .filter(a -> !a.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        //checking splitting result
        if (splitQuery.isEmpty()) {
            logger.warn("Input query '{}' not split correctly.", query);
            throw new QueryException("Split query is empty.",
                    QueryException.ExceptionCause.COMMAND_NOT_FOUND);
        }

        return splitQuery;
    }

    /**
     * Check that users query contains only legal symbols.
     *
     * @param query query from user.
     * @throws CommonBotAppException if query has illegal symbols.
     */
    private void checkQueryIllegalSymbols(String query) throws CommonBotAppException {
        if (!query.matches("[a-zA-Z0-9_/,-]+")) {
            logger.trace("Query has unsupported symbols or format: {}", query);
            throw new QueryException("Unsupported symbols in query.",
                    QueryException.ExceptionCause.UNSUPPORTED_SYMBOLS);
        }
    }
}
