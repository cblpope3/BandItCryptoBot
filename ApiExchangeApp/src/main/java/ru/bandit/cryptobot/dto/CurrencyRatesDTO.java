package ru.bandit.cryptobot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRatesDTO {

    @JsonProperty("symbol")
    String symbol;

    @JsonProperty("price")
    String price;

    @Override
    public String toString() {
        return "{" +
                "\"symbol\"=\"" + symbol + '\"' +
                ", \"price\"=\"" + price + '\"' +
                '}';
    }
}