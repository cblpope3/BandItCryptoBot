package ru.bandit.cryptobot.data_containers.triggers;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CurrencyPairEntity {

    private Integer id;

    private CurrencyEntity currency1;

    private CurrencyEntity currency2;
}