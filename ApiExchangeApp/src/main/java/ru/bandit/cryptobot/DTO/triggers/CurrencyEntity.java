package ru.bandit.cryptobot.DTO.triggers;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CurrencyEntity {

    private String currencyNameUser;

    private String currencyNameSource;

    private String currencyFullName;

    private boolean isCrypto;



}