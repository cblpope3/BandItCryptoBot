package ru.bandit.cryptobot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bandit.cryptobot.entities.UserEntity;

/**
 * Class used to transfer user information between other classes.
 *
 * @see #userId
 * @see #chatId
 * @see #firstName
 * @see #lastName
 * @see #username
 * @see #isBot
 * @see #language
 * @see #lastMessageId
 * @see #generateNewUser()
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * True, if this user is a bot.
     */
    boolean isBot;

    /**
     * Unique identifier for this user or bot.
     */
    private Long userId;

    /**
     * Telegram chat id.
     */
    private Long chatId;

    /**
     * User first name.
     */
    private String firstName;

    /**
     * User last name. Optional.
     */
    private String lastName;

    /**
     * User nickname. Optional.
     */
    private String username;

    /**
     * User language zone.
     */
    private String language;

    /**
     * Last user message id.
     */
    private Integer lastMessageId;

    /**
     * Make instance using Telegram built-in classes.
     *
     * @param user          user as {@link User}.
     * @param chatId        users chat id.
     * @param lastMessageId id of message to edit (for keyboard).
     */
    public UserDTO(User user, Long chatId, Integer lastMessageId) {
        this.isBot = user.getIsBot();
        this.userId = user.getId();
        this.chatId = chatId;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUserName();
        this.language = user.getLanguageCode();
        this.lastMessageId = lastMessageId;
    }

    /**
     * Generate template of {@link UserEntity} to save it to database.
     *
     * @return ready to save user entity as {@link UserEntity}
     */
    public UserEntity generateNewUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(this.userId);
        userEntity.setChatId(this.chatId);
        userEntity.setBot(this.isBot);
        userEntity.setFirstName(this.firstName);
        userEntity.setLastName(this.lastName);
        userEntity.setUsername(this.username);
        userEntity.setLanguage(this.language);
        userEntity.setLastMessageId(this.lastMessageId);

        return userEntity;
    }
}
