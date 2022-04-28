package ru.bandit.cryptobot.dao;

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
    }


    /**
     * Get correct menu item by its name.
     *
     * @param command name of requested item
     * @return found menu item.
     * @throws CommonBotAppException if requested menu item is not found.
     */
    public AbstractMenuItem findMenuItem(String command) throws CommonBotAppException {
        AbstractMenuItem foundItem = this.availableCommands.get(command);
        if (foundItem == null) {
            throw new QueryException("Requested command not found.", QueryException.ExceptionCause.COMMAND_NOT_FOUND);
        } else {
            return foundItem;
        }
    }
}
