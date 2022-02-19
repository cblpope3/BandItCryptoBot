package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metrics")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MetricsEntity {
    @Id
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "help_count", nullable = false)
    Long helpCount;

    @Column(name = "text_command_count", nullable = false)
    Long textCommandCount;

    @Column(name = "interactive_command_count", nullable = false)
    Long interactiveCommandCount;

    @Column(name = "donate_count", nullable = false)
    Long donateCount;
}
