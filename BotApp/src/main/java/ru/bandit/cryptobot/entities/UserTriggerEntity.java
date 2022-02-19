package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @JoinColumn(name = "telegram_userchat_id", nullable = false)
    UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pair_id", nullable = false)
    CurrencyPairEntity currencyPair;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trigger_type_id", nullable = false)
    TriggerTypeEntity triggerType;
    Integer targetValue;

    //TODO decide if this needed
    @Column(name = "last_message")
    String lastMessage;

    //TODO decide if this needed
    @Column(name = "is_archived")
    boolean isArchived;
}
