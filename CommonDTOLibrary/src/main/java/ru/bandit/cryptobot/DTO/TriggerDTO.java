package ru.bandit.cryptobot.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    public enum TriggerType {
        UP,
        DOWN
    }
}
