package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "currency_pair")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CurrencyPairEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_pair_id_generator")
    @SequenceGenerator(name = "currency_pair_id_generator", sequenceName = "currency_pair_sequence", allocationSize = 1)
    @Column(name = "pair_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_1", nullable = false)
    private CurrencyEntity currency1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_2", nullable = false)
    private CurrencyEntity currency2;
}