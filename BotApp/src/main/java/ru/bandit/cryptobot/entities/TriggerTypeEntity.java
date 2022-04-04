package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represent trigger type entity. Have the following parameters: {@link #id}, {@link #triggerName}, and {@link #triggerDescription}.
 */
@Entity
@Table(name = "trigger_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTypeEntity {

    /**
     * Trigger type id as stored in database. Autoincrement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_type_id_generator")
    @SequenceGenerator(name = "trigger_type_id_generator", sequenceName = "trigger_id_sequence", allocationSize = 1)
    @Column(name = "trigger_type_id", nullable = false)
    private Integer id;

    /**
     * Short trigger type name.
     */
    @Column(name = "trigger_type_name", nullable = false)
    private String triggerName;

    /**
     * Description of trigger type.
     */
    @Column(name = "trigger_type_description")
    private String triggerDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerTypeEntity that = (TriggerTypeEntity) o;
        return id.equals(that.id) && triggerName.equals(that.triggerName) && Objects.equals(triggerDescription, that.triggerDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, triggerName, triggerDescription);
    }
}