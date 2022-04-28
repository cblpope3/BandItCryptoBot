package ru.bandit.cryptobot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

/**
 * Class implements bot menu item "average". Parent is {@link Menu01Main}.
 *
 * @see AbstractMenuItem
 * @see Menu01Main
 */
@Component
public class MenuAverage extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuAverage(Menu01Main parent, TriggersService triggersService) {
        super(parent, 2);
        this.triggersService = triggersService;
    }

    /**
     * Command name of this menu item is <b>average</b>.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "average";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            triggersService.subscribe(userDTO, new CurrencyPairDTO(queryParams.get(0), queryParams.get(1)),
                    "average");
            return "Подписка создана успешно.";
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }
    }
}
