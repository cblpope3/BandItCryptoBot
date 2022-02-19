package ru.bandit.cryptobot.data_containers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceResponse {

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