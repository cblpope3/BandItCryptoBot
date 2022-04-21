package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.services.UsersService;

import java.util.List;

/**
 * Class implements bot main menu. Have no parent menu item.
 *
 * @see AbstractMenuItem
 */
@Component
public class Menu01Main extends AbstractMenuItem {

    private final UsersService usersService;

    @Autowired
    public Menu01Main(UsersService usersService) {
        //this is main menu and have no parent.
        super(null);
        this.usersService = usersService;
    }

    /**
     * Command name of this menu item is <b>start</b>.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "start";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "Выберите желаемое действие:";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InlineKeyboardMarkup getMarkup() {

        //maybe better make unchangeable buttons final?
        List<List<InlineKeyboardButton>> buttonsGrid = List.of(
                this.makeHugeButton("Доступные валюты", "all_cur"),
                this.makeHugeButton("Операции с валютами", "operations"),
                usersService.isUserPaused(userDTO) ?
                        this.makeHugeButton("Возобновить подписки", "resume") :
                        this.makeHugeButton("Пауза", "pause"),
                this.makeHugeButton("Стоп", "stop_confirm"),
                this.makeHugeButton("Помощь", "help")
        );

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}