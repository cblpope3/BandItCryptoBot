package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Represent metrics data that stores in database. Have only one row in whole table.
 *
 * @see #id
 * @see #helpCount
 * @see #textCommandCount
 * @see #interactiveCommandCount
 * @see #donateCount
 */
@Entity
@Table(name = "metrics")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetricsEntity {

    /**
     * id of this single row in database. It's always equals 1.
     */
    @Id
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Counter of help command entered by users.
     */
    @Column(name = "help_count", nullable = false)
    Long helpCount;

    /**
     * Counter of text commands entered by users.
     */
    @Column(name = "text_command_count", nullable = false)
    Long textCommandCount;

    /**
     * Counter of button press in bot menu.
     */
    @Column(name = "interactive_command_count", nullable = false)
    Long interactiveCommandCount;

    /**
     * Counter of donations. Not used in current version of application, implemented for future use.
     */
    @Column(name = "donate_command_count", nullable = false)
    Long donateCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsEntity metrics = (MetricsEntity) o;
        return id.equals(metrics.id) && helpCount.equals(metrics.helpCount) && textCommandCount.equals(metrics.textCommandCount) && interactiveCommandCount.equals(metrics.interactiveCommandCount) && Objects.equals(donateCount, metrics.donateCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, helpCount, textCommandCount, interactiveCommandCount, donateCount);
    }
}
