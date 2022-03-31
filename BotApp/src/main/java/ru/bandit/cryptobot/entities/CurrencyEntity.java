package ru.bandit.cryptobot.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Represent currency entity. Have the following parameters: {@link #currencyNameUser}, {@link #currencyNameSource},
 * {@link #currencyFullName} and {@link #isCrypto}.
 */
@Entity
@Table(name = "currency")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity {

    /**
     * User-friendly currency symbol.
     */
    @Id
    @Column(name = "currency_id", nullable = false)
    private String currencyNameUser;

    /**
     * Currency symbol as given in remote api.
     */
    @Column(name = "currency_symbol", nullable = false)
    private String currencyNameSource;

    /**
     * Currency full name.
     */
    @Column(name = "currency_name", nullable = false)
    private String currencyFullName;

    /**
     * Is this currency crypto-currency.
     */
    @Column(name = "is_crypto", nullable = false)
    private boolean isCrypto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CurrencyEntity currency = (CurrencyEntity) o;
        return currencyNameUser != null && Objects.equals(currencyNameUser, currency.currencyNameUser);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}