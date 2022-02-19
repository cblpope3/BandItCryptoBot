package ru.bandit.cryptobot.data_containers;

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

    @JsonProperty("USDTRUB")
    private Float usdTRub;

    @JsonProperty("BNBRUB")
    private Float bnbRub;

    @JsonProperty("XRPRUB")
    private Float xrpRub;

    @JsonProperty("ADARUB")
    private Float adaRub;
}