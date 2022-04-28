package ru.bandit.cryptobot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Class implementing bot menu item that generate 'select trigger type' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuTriggerType extends AbstractMenuItem {

    protected MenuTriggerType(MenuOperations parent) {
        super(parent, 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "trigger_type";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Выберите тип триггера:";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(
                this.makeHugeButton("Текущий курс",
                        "/once/" + String.join("/", queryParams)),
                this.makeHugeButton("Рассылка",
                        "/simple/" + String.join("/", queryParams)),
                this.makeHugeButton("Среднее за минуту",
                        "/average/" + String.join("/", queryParams)),
                this.makeHugeButton("Будильник",
                        "/target/" + String.join("/", queryParams)),
                this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
