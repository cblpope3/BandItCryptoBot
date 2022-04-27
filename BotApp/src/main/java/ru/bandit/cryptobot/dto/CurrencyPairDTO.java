package ru.bandit.cryptobot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyPairDTO {
    private String currency1Name;
    private String currency2Name;

    public List<String> getCurrenciesList() {
        return List.of(currency1Name, currency2Name);
    }
}
