package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Class implementing bot menu operations with subscriptions item.
 *
 * @see AbstractMenuItem
 */
@Component
public class MenuOperations extends AbstractMenuItem {

    @Autowired
    protected MenuOperations(Menu01Main parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "operations";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Что хотите сделать с валютами?";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        List<InlineKeyboardButton> keyboardRow1 = List.of(
                this.makeButton("Подписаться", "cur_1_select"),
                this.makeButton("Отписаться", "unsubscribe_select"),
                this.makeButton("Список", "my_subscriptions")
        );

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}