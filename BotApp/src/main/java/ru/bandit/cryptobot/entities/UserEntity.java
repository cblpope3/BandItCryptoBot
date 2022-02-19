package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserEntity {
    @Id
    @Column(name = "telegram_userchat_id", nullable = false)
    private Long chatId;

    @Column(name = "user_name", nullable = false)
    private String chatName;

    @Column(name = "update_interval")
    private Integer updateInterval;

    @Column(name = "is_paused", nullable = false)
    private boolean isPaused;
    
    @Column(name = "registration_date")
    private Long registrationDate;

    @Column(name = "start_count")
    private Long startCount;
}