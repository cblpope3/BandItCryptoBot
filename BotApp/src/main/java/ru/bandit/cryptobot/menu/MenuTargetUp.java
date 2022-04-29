package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle 'create target_up trigger' command.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuTargetUp extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuTargetUp(Menu01Main parent, TriggersService triggersService) {
        super(parent, 3);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "target_up";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            UserTriggerEntity savedTrigger = triggersService.subscribe(userDTO,
                    new CurrencyPairDTO(queryParams.get(0), queryParams.get(1)),
                    "target_up",
                    Double.parseDouble(queryParams.get(2)));

            return String.format("Следим за превышением курса %s/%s выше порога %s.",
                    savedTrigger.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                    savedTrigger.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                    savedTrigger.getTargetValue());

        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
