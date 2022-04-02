package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CurrenciesTestData extends CommonTestData {
    public static Map<String, Double> getTestCurrencyRates() {
        return Map.of(currencyPairBTCEUR, currencyPairBTCEURValue, currencyPairETHRUB, currencyPairETHRUBValue);
    }

    public static String getCurrencyPairBTCEURSymbol() {
        return currencyPairBTCEUR;
    }

    public static String getCurrencyPairETHRUBSymbol() {
        return currencyPairETHRUB;
    }

    public static String getCurrencyPairBTCRUBSymbol() {
        return currencyPairBTCRUB;
    }

    public static String getCurrencyPairETHEURSymbol() {
        return currencyPairETHEUR;
    }

    public static Double getCurrencyPairBTCEURValue() {
        return currencyPairBTCEURValue;
    }

    public static String getCurrencyBTCSymbol() {
        return currencySymbolBTC;
    }

    public static String getCurrencyETHSymbol() {
        return currencySymbolETH;
    }

    public static String getCurrencyEURSymbol() {
        return currencySymbolEUR;
    }

    public static String getCurrencyRUBSymbol() {
        return currencySymbolRUB;
    }

    public static CurrencyEntity getCurrencyBTC() {
        return new CurrencyEntity(currencySymbolBTC, currencySymbolBTC, currencySymbolBTC, true);
    }

    public static CurrencyEntity getCurrencyETH() {
        return new CurrencyEntity(currencySymbolETH, currencySymbolETH, currencySymbolETH, true);
    }

    public static CurrencyEntity getCurrencyEUR() {
        return new CurrencyEntity(currencySymbolEUR, currencySymbolEUR, currencySymbolEUR, false);
    }

    public static CurrencyEntity getCurrencyRUB() {
        return new CurrencyEntity(currencySymbolRUB, currencySymbolRUB, currencySymbolRUB, false);
    }

    public static CurrencyPairEntity getCurrencyPairBTCEUR() {
        return new CurrencyPairEntity(0, getCurrencyBTC(), getCurrencyEUR());
    }

    public static CurrencyPairEntity getCurrencyPairBTCRUB() {
        return new CurrencyPairEntity(1, getCurrencyBTC(), getCurrencyRUB());
    }

    public static CurrencyPairEntity getCurrencyPairETHEUR() {
        return new CurrencyPairEntity(2, getCurrencyETH(), getCurrencyEUR());
    }

    public static CurrencyPairEntity getCurrencyPairETHRUB() {
        return new CurrencyPairEntity(3, getCurrencyETH(), getCurrencyRUB());
    }

    public static CurrencyPairEntity getCurrencyPairBTCETH() {
        return new CurrencyPairEntity(4, getCurrencyBTC(), getCurrencyETH());
    }

    public static List<CurrencyEntity> getCurrenciesList() {
        return List.of(new CurrencyEntity(currencySymbolBTC, currencySymbolBTC, currencySymbolBTC, true),
                new CurrencyEntity(currencySymbolETH, currencySymbolETH, currencySymbolETH, true),
                new CurrencyEntity(currencySymbolEUR, currencySymbolEUR, currencySymbolEUR, false),
                new CurrencyEntity(currencySymbolRUB, currencySymbolRUB, currencySymbolRUB, false));
    }

    public static Set<CurrencyEntity> getCurrenciesSet() {
        return Set.of(new CurrencyEntity(currencySymbolBTC, currencySymbolBTC, currencySymbolBTC, true),
                new CurrencyEntity(currencySymbolETH, currencySymbolETH, currencySymbolETH, true),
                new CurrencyEntity(currencySymbolEUR, currencySymbolEUR, currencySymbolEUR, false),
                new CurrencyEntity(currencySymbolRUB, currencySymbolRUB, currencySymbolRUB, false));
    }
}
