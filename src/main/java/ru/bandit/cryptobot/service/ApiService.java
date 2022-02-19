package ru.bandit.cryptobot.service;

import java.util.Map;

public interface ApiService {
    public Map<String, Double> getAllCurrencyPrices();
}