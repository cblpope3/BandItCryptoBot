package ru.bandit.cryptobot.menu;

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

        List<List<InlineKeyboardButton>> keyboard = List.of(
                this.makeHugeButton("Подписаться", "cur_1_select"),
                this.makeHugeButton("Отписаться", "unsubscribe_select"),
                this.makeHugeButton("Список", "my_subscriptions"),
                this.getBackButton()
        );

        return new InlineKeyboardMarkup(keyboard);
    }
}