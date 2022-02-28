package ru.bandit.cryptobot.test_data;

import java.util.List;
import java.util.Map;

public class RatesDAOTestData {
    public static String testRateSymbol1 = "BTCRUB";
    public static String testRateSymbol2 = "ETHRUB";

    public static Double testRateValue1_1 = 100.1;
    public static Double testRateValue2_1 = 200.2;
    public static Double testRateValue1_2 = 300.3;
    public static Double testRateValue2_2 = 400.4;

    public static Map<String, Double> testCurrencyRatesMap1 = Map.of(testRateSymbol1, testRateValue1_1,
            testRateSymbol2, testRateValue2_1);
    public static Map<String, Double> testCurrencyRatesMap2 = Map.of(testRateSymbol1, testRateValue1_2,
            testRateSymbol2, testRateValue2_2);

    public static List<Map<String, Double>> testArchiveRates = List.of(testCurrencyRatesMap1, testCurrencyRatesMap2);

    public static Map<String, Double> getTestAverageRates() {
        return Map.of(testRateSymbol1, (testRateValue1_1 + testRateValue1_2) / 2,
                testRateSymbol2, (testRateValue2_1 + testRateValue2_2) / 2);
    }
}
