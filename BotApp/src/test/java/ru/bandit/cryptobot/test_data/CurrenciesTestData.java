package ru.bandit.cryptobot.test_data;

import java.util.Map;

public class CurrenciesTestData extends CommonTestData {
    public static Map<String, Double> getTestCurrencyRates() {
        return Map.of(currency1Symbol, currency1Value, currency2Symbol, currency2Value);
    }

    public static String getCurrency1Symbol() {
        return currency1Symbol;
    }

    public static Double getCurrency1Value() {
        return currency1Value;
    }
}
