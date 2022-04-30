package ru.bandit.cryptobot.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.QueryException;
import ru.bandit.cryptobot.menu.AbstractMenuItem;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class create all available commands list at startup and stores it.
 *
 * @see AbstractMenuItem
 */
@Component
public class BotCommandsDAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, AbstractMenuItem> availableCommands;

    /**
     * At startup this constructor getting all {@link AbstractMenuItem} child classes from spring context and generating
     * {@link Map} of registered command name -> corresponding menu item.
     *
     * @param context spring framework application context.
     */
    @Autowired
    public BotCommandsDAO(ApplicationContext context) {
        //Getting all available menu items from application context.
        //Assuming that all menu items must be child to AbstractMenuItem abstract class
        this.availableCommands = context.getBeansOfType(AbstractMenuItem.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(menuEntry -> menuEntry.getValue().getCommandName(), Map.Entry::getValue));
        if (logger.isInfoEnabled()) {
            logger.info("Available bot commands found: ");
            this.availableCommands.forEach((key, value) -> logger.info(key));
        }
    }


    /**
     * Get correct menu item by its name.
     *
     * @param command name of requested item
     * @return found menu item.
     * @throws CommonBotAppException if requested menu item is not found.
     */
    public AbstractMenuItem findMenuItem(String command) throws CommonBotAppException {
        if (logger.isTraceEnabled()) logger.trace("Trying to find corresponding menu item to command '{}'", command);
        AbstractMenuItem foundItem = this.availableCommands.get(command);
        if (foundItem == null) {
            if (logger.isTraceEnabled()) logger.trace("Command '{}' not recognized.", command);
            throw new QueryException("Requested command not found.", QueryException.ExceptionCause.COMMAND_NOT_FOUND);
        } else {
            if (logger.isTraceEnabled())
                logger.trace("Found menu item that matches command '{}': {}", command, foundItem);
            return foundItem;
        }
    }
}
