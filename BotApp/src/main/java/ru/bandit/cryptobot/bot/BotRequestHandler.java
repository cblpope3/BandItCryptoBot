package ru.bandit.cryptobot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.bot.menu.AbstractMenuItem;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.QueryService;

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

        QueryDTO queryDTO;

        //processing input query into reliable QueryDTO
        try {
            queryDTO = queryService.getQueryDTO(query);
        } catch (CommonBotAppException e) {
            //todo return error page
            logger.error("!!! Unhandled exception: {}", e.getMessage());
            return null;
        }
        //todo what if menu item not found
        AbstractMenuItem foundMenuItem = availableCommands.get(queryDTO.getCommandName());
        foundMenuItem.setQueryDTO(queryDTO);
        foundMenuItem.setUserDTO(user);
        return foundMenuItem;

    }
}
