package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
            InlineKeyboardButton backButton = new InlineKeyboardButton("Назад");
            backButton.setCallbackData("/start");
            response = new BotResponseDTO(
                    new InlineKeyboardMarkup(List.of(List.of(backButton))), e.getUserFriendlyMessage());
        }

        if (logger.isTraceEnabled()) logger.trace("Response to user #{} created successfully.", user.getUserId());
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
            if (logger.isDebugEnabled()) logger.debug("Input query '{}' is empty after splitting.", query);
            throw new QueryException("Split query is empty.",
                    QueryException.ExceptionCause.COMMAND_NOT_FOUND);
        }

        if (logger.isTraceEnabled()) logger.trace("Query '{}' have been split to {}.", query, splitQuery);

        return splitQuery;
    }

    /**
     * Check that users query contains only legal symbols.
     *
     * @param query query from user.
     * @throws CommonBotAppException if query has illegal symbols.
     */
    private void checkQueryIllegalSymbols(String query) throws CommonBotAppException {
        if (!query.matches("[a-zA-Z0-9_/,.-]+")) {
            if (logger.isDebugEnabled()) logger.debug("Query has unsupported symbols or format: {}", query);
            throw new QueryException("Unsupported symbols in query.",
                    QueryException.ExceptionCause.UNSUPPORTED_SYMBOLS);
        }
        if (logger.isTraceEnabled()) logger.trace("Query '{}' passed symbols check.", query);
    }
}
