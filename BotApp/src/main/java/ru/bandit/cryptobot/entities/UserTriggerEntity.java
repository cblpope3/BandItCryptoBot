package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_triggers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTriggerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_trigger_id_generator")
    @SequenceGenerator(name = "user_trigger_id_generator", sequenceName = "user_trigger_id_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "telegram_user_id", nullable = false)
    UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pair_id", nullable = false)
    CurrencyPairEntity currencyPair;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trigger_type_id", nullable = false)
    TriggerTypeEntity triggerType;

    @Column(name = "trigger_target_value")
    Integer targetValue;

    @Column(name = "last_message")
    Timestamp lastMessage;

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
