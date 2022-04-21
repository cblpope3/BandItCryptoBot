package ru.bandit.cryptobot.bot.menu;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.Collections;
import java.util.List;

/**
 * Abstract template for bot menu items. To create new bot menu item follow this steps:<br>
 * 1. Create new class.<br>
 * 2. New class should extend this one.<br>
 * 3. Implement constructor for this class. Constructor must call {@code super()} method with parent menu item as
 * parameter ({@link AbstractMenuItem} instance).<br>
 * 4. Implement {@link #registerCommand()} method. This method must return menu item name as {@link String}.
 * Method name must be unique.<br>
 * 5. Annotate new class with {@link Component} annotation.<br>
 */
@Component
public abstract class AbstractMenuItem {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Setter
    protected UserDTO userDTO;

    @Setter
    protected QueryDTO queryDTO;

    @Getter
    protected String commandName;

    protected AbstractMenuItem parent;

    /**
     * Default constructor for this abstract class. Need parent menu item as parameter.
     *
     * @param parent parent menu item.
     */
    protected AbstractMenuItem(AbstractMenuItem parent) {
        this.commandName = registerCommand();
        this.parent = parent;
    }

    /**
     * Method to register menu item command name. This method must return command name as {@link String}.
     * <b>Command name must be unique!</b>
     *
     * @return command name to access this menu item from telegram.
     */
    protected abstract String registerCommand();

    /**
     * Method that generates response message text.
     *
     * @return message for user.
     */
    public abstract String getText();

    /**
     * Method that generates keyboard for users response. Generating only "back" button by default.
     * If other keyboard layout needed, this method must be overridden.
     *
     * @return keyboard layout for response.
     * @see #getBackButton()
     */
    public InlineKeyboardMarkup getMarkup() {
        return new InlineKeyboardMarkup(List.of(this.getBackButton()));
    }

    /**
     * Method generates "back" button. This button leads to parent menu item.
     *
     * @return back button as single row ({@link List} of {@link InlineKeyboardButton}).
     */
    protected List<InlineKeyboardButton> getBackButton() {

        if (parent == null) return Collections.emptyList();
        else {
            return this.makeHugeButton("Назад", parent.getCommandName());
        }
    }

    /**
     * Make new inline keyboard button by defining its name and command.
     *
     * @param name title of button.
     * @param path command that will be sent to bot if button is pressed.
     * @return new {@link InlineKeyboardButton} with given parameters.
     * @see #makeHugeButton(String, String)
     */
    protected InlineKeyboardButton makeButton(String name, String path) {
        InlineKeyboardButton newButton = new InlineKeyboardButton(name);
        newButton.setCallbackData(path);
        return newButton;
    }

    /**
     * Make one button that fills all line in keyboard layout.
     *
     * @param name title of button.
     * @param path command that will be sent to bot if button is pressed.
     * @return new {@link List} of {@link InlineKeyboardButton} with given parameters.
     * @see #makeButton(String, String)
     */
    protected List<InlineKeyboardButton> makeHugeButton(String name, String path) {
        return List.of(this.makeButton(name, path));
    }
}
