package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Class implementing bot menu item that generate 'select target trigger type' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuTarget extends AbstractMenuItem {

    protected MenuTarget(MenuOperations parent) {
        super(parent, 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "target";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Выберите тип будильника:";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(
                this.makeHugeButton("Выше порога",
                        "/value/" + String.join("/", queryParams) + "/target_up/10"),
                this.makeHugeButton("Ниже порога",
                        "/value/" + String.join("/", queryParams) + "/target_down/10"),
                this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
