package ru.bandit.cryptobot.DAO;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores all 1-minute average currency rates. Implements singleton design pattern.
 */
@Component
public class Avg1MinuteRatesDAO {

    private final Map<String, Double> averageCurrencyRates = new HashMap<>();

    /**
     * Get all average rates stored in this class
     *
     * @return {@link Map} <{@link String}, {@link Double}> of currency rates
     */
    public Map<String, Double> getAverageCurrencyRates() {
        return averageCurrencyRates;
    }

    /**
     * Replace all stored average currency rates by new {@link List} of {@link UserTriggerEntity}
     *
     * @param averageCurrencyRates {@link Map} <{@link String}, {@link Double}> of new currency rates to save.
     */
    public void setAverageCurrencyRates(Map<String, Double> averageCurrencyRates) {
        this.averageCurrencyRates.clear();
        this.averageCurrencyRates.putAll(averageCurrencyRates);
    }
}
