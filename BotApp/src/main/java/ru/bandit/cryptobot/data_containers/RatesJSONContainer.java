package ru.bandit.cryptobot.data_containers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatesJsonContainer {
    @JsonProperty("symbol")
    String symbol;

    @JsonProperty("price")
    String price;

}
