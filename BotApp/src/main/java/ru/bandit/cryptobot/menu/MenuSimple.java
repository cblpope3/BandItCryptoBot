package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implementing bot menu item that handle 'create simple subscription' command.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuSimple extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuSimple(Menu01Main parent, TriggersService triggersService) {
        super(parent, 2);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "simple";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            UserTriggerEntity savedTrigger = triggersService.subscribe(userDTO, new CurrencyPairDTO(queryParams.get(0), queryParams.get(1)),
                    "simple");
            return String.format("Подписка на %s/%s создана успешно.",
                    savedTrigger.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                    savedTrigger.getCurrencyPair().getCurrency2().getCurrencyNameUser());
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
