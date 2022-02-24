package ru.bandit.cryptobot.DTO.triggers;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserTriggerEntity {

    Long id;

    UserEntity user;

    CurrencyPairEntity currencyPair;

    TriggerTypeEntity triggerType;

    Integer targetValue;

    Timestamp lastMessage;

    boolean isArchived;
}
