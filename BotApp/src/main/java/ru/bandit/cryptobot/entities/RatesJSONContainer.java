package ru.bandit.cryptobot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatesJSONContainer {
    //TODO decide what format is better for transaction between apps
    @JsonProperty("BTCRUB")
    private Float btcRub;

    @JsonProperty("ETHRUB")
    private Float ethRub;

}