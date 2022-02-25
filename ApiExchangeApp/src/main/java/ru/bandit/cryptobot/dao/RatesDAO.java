package ru.bandit.cryptobot.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores all currency rates. Implements singleton design pattern.
 */
@Component
public class RatesDAO {

    private final Map<String, Double> currencyRates = new HashMap<>();

    /**
     * Get all rates stored in this class
     *
     * @return {@link Map} <{@link String}, {@link Double}> of currency rates
     */
    public Map<String, Double> getCurrencyRates() {
        return currencyRates;
    }

    /**
     * Replace all stored currency rates by new {@link Map} of currency rates
     *
     * @param currencyRates {@link Map} <{@link String}, {@link Double}> of new currency rates to save.
     */
    public void setCurrencyRates(Map<String, Double> currencyRates) {
        this.currencyRates.clear();
        this.currencyRates.putAll(currencyRates);
    }
}
