package ru.bandit.cryptobot.DTO.triggers;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserEntity {
    private Long chatId;

    private String chatName;

    private Integer updateInterval;

    private boolean isPaused;
}