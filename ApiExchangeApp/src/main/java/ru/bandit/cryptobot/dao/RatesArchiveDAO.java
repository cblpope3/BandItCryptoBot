package ru.bandit.cryptobot.dao;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class stores previous currency rates. Implements singleton design pattern.
 */
@Component
public class RatesArchiveDAO {

    private final LinkedList<Map<String, Double>> currencyRatesArchive = new LinkedList<>();

    /**
     * Get all currency rates stored in archive
     *
     * @return {@link LinkedList} of {@link Map}. Latest first.
     */
    public List<Map<String, Double>> getCurrencyRatesArchive() {
        return currencyRatesArchive;
    }

    /**
     * Add new rates to archive
     *
     * @param newRates {@link Map} of new currency rates.
     */
    public void appendNewRatesToArchive(Map<String, Double> newRates) {
        currencyRatesArchive.addFirst(Map.copyOf(newRates));

        //size of linked list (12) is hardcoded based on 5-seconds period of new data requests
        if (currencyRatesArchive.size() > 12) currencyRatesArchive.removeLast();
    }
}
