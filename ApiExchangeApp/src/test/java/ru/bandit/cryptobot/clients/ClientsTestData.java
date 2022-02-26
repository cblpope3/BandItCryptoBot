package ru.bandit.cryptobot.clients;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;

public class ClientsTestData {

    private final static CurrencyRatesDTO currencyRates1 = new CurrencyRatesDTO();
    private final static CurrencyRatesDTO currencyRates2 = new CurrencyRatesDTO();

    private static String expectedResponse;

    public static String getExpectedResponse() {
        fillTestData();
        return expectedResponse;
    }

    public static List<CurrencyRatesDTO> getTestData() {
        fillTestData();
        return List.of(currencyRates1, currencyRates2);
    }

    private static void fillTestData(){
        currencyRates1.setSymbol("BTCRUB");
        currencyRates1.setPrice("100.1");

        currencyRates2.setSymbol("ETHRUB");
        currencyRates2.setPrice("200.2");

        expectedResponse = String.format("[{\"symbol\":\"%s\",\"price\":\"%s\"},{\"symbol\":\"%s\",\"price\":\"%s\"}]",
                currencyRates1.getSymbol(), currencyRates1.getPrice(),
                currencyRates2.getSymbol(), currencyRates2.getPrice());
    }
}