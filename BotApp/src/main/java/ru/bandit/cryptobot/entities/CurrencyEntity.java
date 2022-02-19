package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currency")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CurrencyEntity {

    @Id
    @Column(name = "currency_id", nullable = false)
    private String currencyNameUser;

    @Column(name = "currency_symbol", nullable = false)
    private String currencyNameSource;

    @Column(name = "currency_name", nullable = false)
    private String currencyFullName;

    @Column(name = "is_crypto", nullable = false)
    private boolean isCrypto;

}