package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents allowed currency pair entity. Have the following parameters: {@link #id}, {@link #currency1} and
 * {@link #currency2}.
 */
@Entity
@Table(name = "currency_pair")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyPairEntity {

    /**
     * Currency pair id as stored in database. Autoincrement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_pair_id_generator")
    @SequenceGenerator(name = "currency_pair_id_generator", sequenceName = "currency_pair_sequence", allocationSize = 1)
    @Column(name = "pair_id", nullable = false)
    private Integer id;

    /**
     * First currency in current currency pair. Represented as {@link CurrencyEntity}
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_1", nullable = false)
    private CurrencyEntity currency1;

    /**
     * Second currency in current currency pair.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_2", nullable = false)
    private CurrencyEntity currency2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPairEntity that = (CurrencyPairEntity) o;
        return id.equals(that.id) && currency1.equals(that.currency1) && currency2.equals(that.currency2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency1, currency2);
    }
}