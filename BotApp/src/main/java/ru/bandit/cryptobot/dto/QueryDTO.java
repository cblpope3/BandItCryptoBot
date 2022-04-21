package ru.bandit.cryptobot.dto;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is for storing and processing queries.
 */
@Getter
@Setter
public class QueryDTO {

    private final Logger logger = LoggerFactory.getLogger(QueryDTO.class);

    private final String commandName;
    private List<String> parameters;

    public QueryDTO(String commandName) {
        this.commandName = commandName;
    }

    public Integer getValue() {
        return Integer.parseInt(this.parameters.get(3));
    }


    public List<String> getCurrencies() {
        return new ArrayList<>(Arrays.asList(this.parameters.get(0), this.parameters.get(1)));
    }

    public String getTriggerType() {
        return this.commandName;
    }

    public String getTriggerValue() {
        //todo this hardcoded if-else must be changed
        if (this.commandName.equals("target_up") || this.commandName.equals("target_down")) {
            return this.parameters.get(2);
        } else if (this.commandName.equals("unsubscribe")) {
            return this.parameters.get(0);
        } else return null;
    }

    @Deprecated(forRemoval = true)
    public String getCurrency1() {
        return this.parameters.get(0);
    }

    @Deprecated(forRemoval = true)
    public String getCurrency2() {
        return this.parameters.get(1);
    }

}
