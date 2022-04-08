package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represent registered trigger/subscription entity.
 *
 * @see #id
 * @see #user
 * @see #currencyPair
 * @see #triggerType
 * @see #targetValue
 * @see #lastMessage
 * @see #isArchived
 */
@Entity
@Table(name = "user_triggers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTriggerEntity {

    /**
     * id of registered trigger as stored in database. Autoincrement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_trigger_id_generator")
    @SequenceGenerator(name = "user_trigger_id_generator", sequenceName = "user_trigger_id_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * User that own this trigger.
     *
     * @see UserEntity
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "telegram_user_id", nullable = false)
    UserEntity user;

    /**
     * Currency pair related to trigger.
     *
     * @see CurrencyPairEntity
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "pair_id", nullable = false)
    CurrencyPairEntity currencyPair;

    /**
     * Type of this trigger.
     *
     * @see TriggerTypeEntity
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "trigger_type_id", nullable = false)
    TriggerTypeEntity triggerType;

    /**
     * Target value of this trigger. Nullable.
     */
    @Column(name = "trigger_target_value")
    Integer targetValue;

    /**
     * Last time when trigger have been sent to user. Not used in current version of application. For future use.
     *
     * @see Timestamp
     */
    @Column(name = "last_message")
    Timestamp lastMessage;

    /**
     * Is this trigger is archived. Not used in current version of application. For future use.
     */
    @Column(name = "is_archived")
    boolean isArchived;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTriggerEntity that = (UserTriggerEntity) o;
        return isArchived == that.isArchived && id.equals(that.id) && user.equals(that.user) && currencyPair.equals(that.currencyPair) && triggerType.equals(that.triggerType) && Objects.equals(targetValue, that.targetValue) && Objects.equals(lastMessage, that.lastMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, currencyPair, triggerType, targetValue, lastMessage, isArchived);
    }
}
