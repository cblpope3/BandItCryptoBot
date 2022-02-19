package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "trigger_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_type_id_generator")
    @SequenceGenerator(name = "trigger_type_id_generator", sequenceName = "trigger_id_sequence", allocationSize = 1)
    @Column(name = "trigger_type_id", nullable = false)
    private Integer id;

    @Column(name = "trigger_type_name", nullable = false)
    private String triggerName;

    @Column(name = "trigger_type_description")
    private String triggerDescription;
}