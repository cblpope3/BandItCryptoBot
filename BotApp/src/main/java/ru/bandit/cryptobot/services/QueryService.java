package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.menu.QueryExceptionTesting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public QueryDTO getQueryDTO(String query) throws CommonBotAppException {
        //todo implement this

        //split and verify users query
        List<String> separatedQuery = this.separateQuery(query);

        //select correct command from users query
        QueryDTO queryDTO = new QueryDTO(separatedQuery.remove(0));
//        queryDTO.setCommandName();
        if (!separatedQuery.isEmpty()) queryDTO.setParameters(separatedQuery);
        return queryDTO;

    }

    /**
     * Split incoming {@link String} query to {@link List} of commands and make basic validations.
     *
     * @param query incoming query
     * @return split query.
     * @throws CommonBotAppException in case of problems during query validation process.
     */
    private List<String> separateQuery(String query) throws CommonBotAppException {
        //check if query contains unexpected symbols
        if (!query.matches("[a-zA-Z0-9_/,-]+")) {
            logger.trace("Query has unsupported symbols or format: {}", query);
            throw new QueryExceptionTesting("Unsupported symbols in query.",
                    QueryExceptionTesting.ExceptionCause.UNSUPPORTED_SYMBOLS);
        }

        //split query by special letters
        List<String> splitQuery = Arrays.stream(query.split("[/,-]"))
                .filter(Objects::nonNull)
                .filter(a -> !a.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        //checking splitting result
        if (splitQuery.isEmpty()) {
            logger.warn("Input query '{}' not split correctly.", query);
            throw new QueryExceptionTesting("Split query is empty.",
                    QueryExceptionTesting.ExceptionCause.NOT_FOUND_COMMAND);
        }

        return splitQuery;
    }
}
