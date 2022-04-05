package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represent user entity. Have the following parameters:
 *
 * @see #userId
 * @see #isBot
 * @see #chatId
 * @see #firstName
 * @see #lastName
 * @see #username
 * @see #language
 * @see #lastMessageId
 * @see #updateInterval
 * @see #isPaused
 * @see #registrationDate
 * @see #startCount
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /**
     * True, if this user is a bot.
     */
    @Column(name = "is_bot", nullable = false)
    boolean isBot;
    /**
     * Unique identifier for this user or bot.
     */
    @Id
    @Column(name = "telegram_user_id", nullable = false)
    private Long userId;
    /**
     * Telegram chat id.
     */
    @Column(name = "telegram_chat_id", nullable = false)
    private Long chatId;
    /**
     * User first name.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;
    /**
     * User last name. Optional.
     */
    @Column(name = "last_name")
    private String lastName;
    /**
     * User nickname. Optional.
     */
    @Column(name = "username")
    private String username;
    /**
     * User language zone.
     */
    @Column(name = "language")
    private String language;

    /**
     * Last user message id.
     */
    @Column(name = "last_message_id")
    private Integer lastMessageId;

    /**
     * Interval of user subscriptions update. Reserved for future use.
     */
    @Column(name = "update_interval")
    private Integer updateInterval;

    /**
     * Is user paused subscriptions mailing list.
     */
    @Column(name = "is_paused", nullable = false)
    private boolean isPaused;

    /**
     * Users registration date.
     */
    @Column(name = "registration_date")
    private Timestamp registrationDate;

    /**
     * Field for statistic purposes. How many times user entered /start command (i.e. how many times user created new
     * chat with bot).
     */
    @Column(name = "start_count")
    private Long startCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return isBot == user.isBot && isPaused == user.isPaused && userId.equals(user.userId) && chatId.equals(user.chatId) && firstName.equals(user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(username, user.username) && Objects.equals(language, user.language) && Objects.equals(lastMessageId, user.lastMessageId) && Objects.equals(updateInterval, user.updateInterval) && Objects.equals(registrationDate, user.registrationDate) && Objects.equals(startCount, user.startCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, isBot, chatId, firstName, lastName, username, language, lastMessageId, updateInterval, isPaused, registrationDate, startCount);
    }
}