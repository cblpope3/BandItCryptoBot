package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RatesDAOTestData extends CommonData {

    public static Map<String, Double> testCurrencyRatesMap1 = Map.of(testRateSymbol1, testRateValue1_1,
            testRateSymbol2, testRateValue2_1);
    public static Map<String, Double> testCurrencyRatesMap2 = Map.of(testRateSymbol1, testRateValue1_2,
            testRateSymbol2, testRateValue2_2);

    public static List<Map<String, Double>> testArchiveRates = List.of(testCurrencyRatesMap1, testCurrencyRatesMap2);

    public static Map<String, Double> getTestAverageRates() {
        return Map.of(testRateSymbol1, (testRateValue1_1 + testRateValue1_2) / 2,
                testRateSymbol2, (testRateValue2_1 + testRateValue2_2) / 2);
    }

    public static List<CurrencyRatesDTO> getUnfilteredBinanceData() {
        return List.of(currencyRate1, currencyRateNotAllowed1, currencyRate2, currencyRateNotAllowed2);
    }

    public static Map<String, Double> getFilteredBinanceData() {
        return Map.of(testRateSymbol1, testRateValue1_1, testRateSymbol2, testRateValue2_1);
    }

    public static List<CurrencyRatesDTO> getCurrentRatesList() {
        return List.of(currencyRate1, currencyRate2);
    }

    public static Set<CurrencyRatesDTO> getCurrentRatesSet() {
        return Set.of(currencyRate1, currencyRate2);
    }
}
