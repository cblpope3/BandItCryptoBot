package ru.bandit.cryptobot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Class represents trigger. Contains information about it: trigger id ({@link Long});
 * currency pair ({@link String}) of trigger (for example, "BTCEUR");
 * trigger target value ({@link Double}) - threshold value of currency rate;
 * type of trigger ({@link TriggerType}), available values are "UP" and "DOWN".
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerDTO {

    @JsonProperty("triggerId")
    private Long id;

    @JsonProperty("currencyPair")
    private String currencyPair;

    @JsonProperty("targetValue")
    private Double targetValue;

    @JsonProperty("triggerType")
    private TriggerType triggerType;

    /**
     * Enumerate that contains possible trigger type values: "UP" and "DOWN".
     */
    public enum TriggerType {
        UP,
        DOWN
    }
}
