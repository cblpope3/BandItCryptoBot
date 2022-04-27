package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.bot.menu.AbstractMenuItem;
import ru.bandit.cryptobot.bot.menu.MenuError;
import ru.bandit.cryptobot.dao.BotCommandsDAO;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.QueryService;

import java.util.List;

/**
 * This class handle users request and making response to it.
 */
@Component
public class BotRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QueryService queryService;

    @Autowired
    private BotCommandsDAO botCommandsDAO;

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
            AbstractMenuItem foundMenuItem = botCommandsDAO.findMenuItem(commandName);

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
}
