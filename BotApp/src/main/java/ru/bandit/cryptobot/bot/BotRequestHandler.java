package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.bot.menu.AbstractMenuItem;
import ru.bandit.cryptobot.bot.menu.MenuError;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.QueryException;
import ru.bandit.cryptobot.services.QueryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class handle users request and making response to it.
 */
@Component
public class BotRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, AbstractMenuItem> availableCommands;

    @Autowired
    private QueryService queryService;

    @Autowired
    public BotRequestHandler(ApplicationContext context) {
        //Getting all available menu items from application context.
        //Assuming that all menu items must be child to AbstractMenuItem abstract class
        this.availableCommands = context.getBeansOfType(AbstractMenuItem.class)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(menuEntry -> menuEntry.getValue().getCommandName(), Map.Entry::getValue));
    }

    /**
     * Method makes {@link AbstractMenuItem} object as response to users request.
     *
     * @param query users request as {@link QueryDTO}.
     * @param user  data about user that performed request.
     * @return correct response layout with buttons.
     */
    public AbstractMenuItem getMenuLayout(String query, UserDTO user) {

        //processing input query into reliable QueryDTO
        //splitting incoming request to separate parameters
        //extracting requested command name (command name must be first parameter).
        try {
            List<String> separatedQuery = queryService.separateQuery(query);
            String commandName = separatedQuery.remove(0);

            //trying to find command in command map
            AbstractMenuItem foundMenuItem = this.findMenuItem(commandName);

            //creating queryDTO object
            //todo decide if queryDTO is not needed
            QueryDTO queryDTO = new QueryDTO(commandName);
            queryDTO.setParameters(separatedQuery);


            //todo decide if QueryDTO object is useless
            //setting user and query information to chosen menu item
            queryDTO.setParameters(separatedQuery);
            foundMenuItem.setQueryDTO(queryDTO);
            foundMenuItem.setUserDTO(user);
            return foundMenuItem;

        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return new MenuError(e);
        }

    }

    /**
     * Get correct menu item by its name.
     *
     * @param command name of requested item
     * @return found menu item.
     * @throws CommonBotAppException if requested menu item is not found.
     */
    private AbstractMenuItem findMenuItem(String command) throws CommonBotAppException {
        AbstractMenuItem foundItem = this.availableCommands.get(command);
        if (foundItem == null) {
            throw new QueryException("Requested command not found.", QueryException.ExceptionCause.COMMAND_NOT_FOUND);
        } else {
            return foundItem;
        }
    }
}
