package ru.bandit.cryptobot.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RatesRepository {

    private static Map<String, Double> currentRates = new HashMap<>();
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CurrencyPairRepository currencyPairRepository;

    public void saveAll(Map<String, Double> newRates) {
        currentRates = newRates;
    }

    public Double findRatesBySymbol(String symbol) {
        //todo this might be working not good
        return currentRates.get(symbol);
    }
}
