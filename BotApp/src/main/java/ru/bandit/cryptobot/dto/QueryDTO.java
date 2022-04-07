package ru.bandit.cryptobot.dto;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class is for storing and processing queries.
 */
@Getter
@Setter
public class QueryDTO {

    private final Logger logger = LoggerFactory.getLogger(QueryDTO.class);

    //todo strings must be replaced by special classes
    private String command;
    private String parameter1;
    private String parameter2;
    private String parameter3;
    private String parameter4;


    /**
     * Take raw query from user as a {@link String} and process it.
     *
     * @param unprocessedQuery query from user ({@link String}).
     */
    public QueryDTO(String unprocessedQuery) {
        //split raw command into ArrayList of command and parameters
        List<String> separatedQuery = this.splitString(unprocessedQuery);
        if (separatedQuery.isEmpty()) {
            logger.warn("Problem during query processing: not found commands.");
            //todo make properly exception mechanism
            throw new RuntimeException("Can't parse query");

        }

        //todo empty arrayList check
        //try to recognize first command in list
        if (!separatedQuery.isEmpty()) command = separatedQuery.remove(0);
        if (!separatedQuery.isEmpty()) parameter1 = separatedQuery.remove(0);
        if (!separatedQuery.isEmpty()) parameter2 = separatedQuery.remove(0);
        if (!separatedQuery.isEmpty()) parameter3 = separatedQuery.remove(0);
        if (!separatedQuery.isEmpty()) parameter4 = separatedQuery.remove(0);
    }

    public List<String> getCurrencies() {
        return List.of(this.parameter1, this.parameter2);
    }

    public String getTriggerType() {
        return this.command;
    }

    public String getTriggerParameter() {
        return this.parameter3;
    }

    /**
     * Method divide raw {@link String} into separated parameters list.
     *
     * @param unprocessedString incoming {@link String} of user query.
     * @return separated parameters as {@link List} of {@link String}.
     */
    private List<String> splitString(String unprocessedString) {
        return Arrays.stream(unprocessedString.split("[/,]"))
                .filter(Objects::nonNull)
                .filter(a -> !a.isEmpty())
                .collect(Collectors.toList());
    }
}
