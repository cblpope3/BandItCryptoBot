package ru.bandit.cryptobot.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class to store currency rates. Has methods: {@link #getCurrencyRates()}, {@link #setCurrencyRates(Map)},
 * {@link #getRateBySymbol(String)}, {@link #setLastUpdateTime()}, {@link #getLastUpdateTime()},
 */
public abstract class CurrencyRatesDAO {

    protected final Map<String, Double> currentRates = new HashMap<>();
    protected final Timestamp lastUpdateTime = new Timestamp(0);

    /**
     * Get all rates stored in this class
     *
     * @return {@link Map} <{@link String}, {@link Double}> of currency rates
     */
    public Map<String, Double> getCurrencyRates() {
        return currentRates;
    }

    /**
     * Replace all stored currency rates by new {@link Map} and update {@link Timestamp} to current time.
     *
     * @param newRates {@link Map} of currency pair ({@link String}) and value ({@link Double}).
     */
    public void setCurrencyRates(Map<String, Double> newRates) {
        this.currentRates.clear();
        this.currentRates.putAll(newRates);
        //todo take update time from trigger-app, not time when updates was set
        setLastUpdateTime();
    }

    /**
     * Get currency rate by specifying currency rate pair symbol.
     *
     * @param symbol currency pair identifier ({@link String}).
     * @return currency pair rate ({@link Double}).
     */
    public Double getRateBySymbol(String symbol) {
        return currentRates.get(symbol);
    }

    /**
     * Set last update time as current time.
     */
    public void setLastUpdateTime() {
        this.lastUpdateTime.setTime(System.currentTimeMillis());
    }

    /**
     * Get last update time.
     *
     * @return last update time as {@link String}.
     */
    public String getLastUpdateTime() {
        return this.lastUpdateTime.toString();
    }
}