package ru.bandit.cryptobot.test_data;

public abstract class CommonTestData {

    protected static final String currencySymbol1 = "BTC";
    protected static final String currencySymbol2 = "ETH";
    protected static final String currencySymbol3 = "EUR";
    protected static final String currencySymbol4 = "RUB";

    protected static final String currencyPair1 = currencySymbol1 + currencySymbol3;
    protected static final String currencyPair2 = currencySymbol2 + currencySymbol4;

    protected static final Double currency1Value = 100.1;
    protected static final Double currency2Value = 200.1;
}
