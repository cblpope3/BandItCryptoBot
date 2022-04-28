package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dao.BotCommandsDAO;
import ru.bandit.cryptobot.dto.BotResponseDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.QueryException;
import ru.bandit.cryptobot.menu.AbstractMenuItem;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This service class processing user requests and making response.
 */
@Service
public class QueryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BotCommandsDAO botCommandsDAO;

    @Autowired
    public QueryService(BotCommandsDAO botCommandsDAO) {
        this.botCommandsDAO = botCommandsDAO;
    }

    /**
     * Method takes user request and generates response.
     *
     * @param user    information about user that performed this request.
     * @param request users request content.
     * @return response as {@link BotResponseDTO} object.
     */
    public BotResponseDTO makeResponseToUser(UserDTO user, String request) {

        BotResponseDTO response;
        AbstractMenuItem foundMenuItem;

        try {
            List<String> separatedQuery = this.splitQuery(request);
            //assuming that first part of query must be command name
            String commandName = separatedQuery.remove(0);

            foundMenuItem = botCommandsDAO.findMenuItem(commandName);

            response = foundMenuItem.makeResponse(user, separatedQuery);

        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            response = new BotResponseDTO(null, e.getUserFriendlyMessage());

        }

        return response;
    }

    /**
     * Split incoming {@link String} query to {@link List} of commands and make basic validations.
     *
     * @param query incoming query
     * @return split query.
     * @throws CommonBotAppException if query is empty after splitting.
     */
    private List<String> splitQuery(String query) throws CommonBotAppException {

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
