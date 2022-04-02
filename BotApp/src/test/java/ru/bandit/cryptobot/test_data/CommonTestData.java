package ru.bandit.cryptobot.test_data;

public abstract class CommonTestData {

    protected static final String currencySymbolBTC = "BTC";
    protected static final String currencySymbolEUR = "EUR";
    protected static final String currencySymbolETH = "ETH";
    protected static final String currencySymbolRUB = "RUB";

    protected static final String currencyPairBTCEUR = currencySymbolBTC + currencySymbolEUR;
    protected static final String currencyPairETHRUB = currencySymbolETH + currencySymbolRUB;
    protected static final String currencyPairBTCRUB = currencySymbolBTC + currencySymbolRUB;
    protected static final String currencyPairETHEUR = currencySymbolETH + currencySymbolEUR;
    protected static final String currencyPairBTCETH = currencySymbolBTC + currencySymbolETH;

    protected static final Double currencyPairBTCEURValue = 100.1;
    protected static final Double currencyPairETHRUBValue = 200.1;
}
