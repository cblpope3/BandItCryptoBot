package ru.bandit.cryptobot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Class implementing bot menu item that generate 'enter target trigger value' dialog.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuValue extends AbstractMenuItem {

    protected MenuValue(MenuOperations parent) {
        super(parent, 4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "value";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        int value = Integer.parseInt(queryParams.get(3));
        return "Текущий порог срабатывания - " + value + "% от текущего курса. Что следует сделать?";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        int value;
        if (queryParams.get(3) == null) value = 10;
        else value = Integer.parseInt(queryParams.get(3));

        int nextValue = value + 1;
        int prevValue = value - 1;
        if (prevValue < 1) prevValue = 1;

        String currency1 = queryParams.get(0);
        String currency2 = queryParams.get(1);
        String triggerType = queryParams.get(2);

        List<InlineKeyboardButton> keyboardRow1 = List.of(
                this.makeButton("+", "/value/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + nextValue),
                this.makeButton("-", "/value/" + currency1 + "/" + currency2 + "/" + triggerType + "/" + prevValue)
        );

        List<InlineKeyboardButton> keyboardRow2 = this.makeHugeButton("Создать",
                triggerType + "/" + currency1 + "/" + currency2 + "/" + value);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(keyboardRow1, keyboardRow2, this.getBackButton());

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}
