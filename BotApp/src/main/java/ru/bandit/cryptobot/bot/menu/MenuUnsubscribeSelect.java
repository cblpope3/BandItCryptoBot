package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.TriggersService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class implementing bot menu item that generate 'select subscription to unsubscribe' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuUnsubscribeSelect extends AbstractMenuItem {

    private final TriggersService triggersService;

    @Autowired
    protected MenuUnsubscribeSelect(MenuOperations parent, TriggersService triggersService) {
        super(parent);
        this.triggersService = triggersService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "unsubscribe_select";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        try {
            triggersService.getAllUsersSubscriptions(userDTO);
            return "Выберите подписку для удаления:";
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            return e.getUserFriendlyMessage();
        }

    }

    public InlineKeyboardMarkup getMarkup() {

        List<UserTriggerEntity> subscriptionsList;

        try {
            subscriptionsList = triggersService.getAllUsersSubscriptions(userDTO);
        } catch (CommonBotAppException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            subscriptionsList = Collections.emptyList();
        }

        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        for (UserTriggerEntity subscription : subscriptionsList) {

            //FIXME button name is not fine for simple triggers
            //№ - XXX/YYY/Тип триггера/Период/Искомое значение
            InlineKeyboardButton button = new InlineKeyboardButton(
                    String.format("№%d - %s/%s - %s - %d",
                            subscription.getId(),
                            subscription.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                            subscription.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                            subscription.getTriggerType().getTriggerName(),
                            subscription.getTargetValue()));

            button.setCallbackData("unsubscribe/" + subscription.getId().toString());

            List<InlineKeyboardButton> row = List.of(button);

            buttonsGrid.add(row);
        }

        buttonsGrid.add(getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}