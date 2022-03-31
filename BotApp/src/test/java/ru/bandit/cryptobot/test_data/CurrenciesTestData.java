package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.entities.CurrencyEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CurrenciesTestData extends CommonTestData {
    public static Map<String, Double> getTestCurrencyRates() {
        return Map.of(currencyPair1, currency1Value, currencyPair2, currency2Value);
    }

    public static String getCurrencyPair1() {
        return currencyPair1;
    }

    public static Double getCurrency1Value() {
        return currency1Value;
    }

    public static String getCurrency1Symbol() {
        return currencySymbol1;
    }

    public static CurrencyEntity getCurrency1() {
        return new CurrencyEntity(currencySymbol1, currencySymbol1, currencySymbol1, true);
    }

    public static List<CurrencyEntity> getCurrenciesList() {
        return List.of(new CurrencyEntity(currencySymbol1, currencySymbol1, currencySymbol1, true),
                new CurrencyEntity(currencySymbol2, currencySymbol2, currencySymbol2, true),
                new CurrencyEntity(currencySymbol3, currencySymbol3, currencySymbol3, false),
                new CurrencyEntity(currencySymbol4, currencySymbol4, currencySymbol4, false));
    }

    public static Set<CurrencyEntity> getCurrenciesSet() {
        return Set.of(new CurrencyEntity(currencySymbol1, currencySymbol1, currencySymbol1, true),
                new CurrencyEntity(currencySymbol2, currencySymbol2, currencySymbol2, true),
                new CurrencyEntity(currencySymbol3, currencySymbol3, currencySymbol3, false),
                new CurrencyEntity(currencySymbol4, currencySymbol4, currencySymbol4, false));
    }
}
