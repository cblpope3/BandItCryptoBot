package ru.bandit.cryptobot.bot.menu;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.BotResponseDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.QueryException;

import java.util.Collections;
import java.util.List;

/**
 * Abstract template for bot menu items. To create new bot menu item follow this steps:<br>
 * 1. Create new class.<br>
 * 2. New class should extend this one.<br>
 * 3. Implement constructor for this class. Constructor must call {@code super()} method with parent menu item as
 * parameter ({@link AbstractMenuItem} instance).<br>
 * 4. If menu item need some parameters to work properly, specify number of parameters in {@code super()} constructor.
 * 5. Implement {@link #registerCommand()} method. This method must return menu item name as {@link String}.
 * Method name must be unique.<br>
 * 6. Annotate new class with {@link Component} annotation.<br>
 */
@Component
public abstract class AbstractMenuItem {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Getter
    protected final String commandName;
    protected final AbstractMenuItem parent;
    @Getter
    private final int requiredParams;
    protected UserDTO userDTO;
    protected List<String> queryParams;

    /**
     * Default constructor for this abstract class. Need parent menu item and command parameters number.
     *
     * @param parent         parent menu item.
     * @param requiredParams number of required parameters for this menu item.
     */
    protected AbstractMenuItem(AbstractMenuItem parent, int requiredParams) {
        this.commandName = registerCommand();
        this.parent = parent;
        this.requiredParams = requiredParams;
    }

    /**
     * Constructor with 0 command parameters.
     *
     * @param parent parent menu item.
     */
    protected AbstractMenuItem(AbstractMenuItem parent) {
        this(parent, 0);
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

    /**
     * Check given parameters list to match menu item requirements.
     *
     * @param query {@link List} of input parameters as {@link String}.
     * @throws CommonBotAppException if entered wrong parameters number.
     */
    protected void validateInputParameters(List<String> query) throws CommonBotAppException {
        if (query.size() != requiredParams) {
            throw new QueryException("Required parameters number not match.", QueryException.ExceptionCause.WRONG_PARAMETERS_NUMBER);
        }
    }

    /**
     * Method to make response to user request.
     *
     * @param user  data about user.
     * @param query users request to be processed
     * @return message and keyboard layout for user.
     * @throws CommonBotAppException if query parameters number don't match menu item requirements.
     */
    public BotResponseDTO makeResponse(UserDTO user, List<String> query) throws CommonBotAppException {

        //validate query and throw exception if not valid
        this.validateInputParameters(query);

        this.userDTO = user;
        this.queryParams = query;

        BotResponseDTO response = new BotResponseDTO();

        response.setMessage(this.getText());
        response.setKeyboard(this.getMarkup());
        return response;
    }
}
