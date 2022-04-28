package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle 'create target_down trigger' command.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuTargetDown extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuTargetDown(Menu01Main parent, TriggersService triggersService) {
        super(parent, 3);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "target_down";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            triggersService.subscribe(userDTO, new CurrencyPairDTO(queryParams.get(0), queryParams.get(1)),
                    "target_down", Double.parseDouble(queryParams.get(2)));
            return "Будильник создан.";
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
