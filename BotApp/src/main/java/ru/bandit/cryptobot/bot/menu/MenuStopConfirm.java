package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

/**
 * Class implementing bot menu item that generating 'are you sure want to stop?' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuStopConfirm extends AbstractMenuItem {

    protected MenuStopConfirm(Menu01Main parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "stop_confirm";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Вы действительно хотите удалить все подписки? Действие необратимо.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        return new InlineKeyboardMarkup(List.of(
                this.makeHugeButton("Да", "stop"),
                this.getBackButton()
        ));
    }
}