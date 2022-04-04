package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represent user entity. Have the following parameters: {@link #chatId}, {@link #chatName},
 * {@link #updateInterval}, {@link #isPaused}, {@link #registrationDate} and {@link #startCount}.
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
     * Telegram chat id.
     */
    @Id
    @Column(name = "telegram_userchat_id", nullable = false)
    private Long chatId;

    /**
     * User first name.
     */
    @Column(name = "user_name", nullable = false)
    private String chatName;

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
        return isPaused == user.isPaused && chatId.equals(user.chatId) && chatName.equals(user.chatName) && Objects.equals(updateInterval, user.updateInterval) && Objects.equals(registrationDate, user.registrationDate) && Objects.equals(startCount, user.startCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatName, updateInterval, isPaused, registrationDate, startCount);
    }
}