package ru.bandit.cryptobot.data_containers.triggers;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TriggerTypeEntity {

    private Integer id;

    private String triggerName;

    private String triggerDescription;
}