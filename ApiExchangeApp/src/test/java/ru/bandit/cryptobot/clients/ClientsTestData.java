package ru.bandit.cryptobot.clients;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientsTestData {

    private final static CurrencyRatesDTO currencyRates1 = new CurrencyRatesDTO("BTCRUB", "100.1");
    private final static CurrencyRatesDTO currencyRates2 = new CurrencyRatesDTO("ETHRUB", "200.2");

    private final static List<CurrencyRatesDTO> testArrayList = new ArrayList<>(Arrays.asList(currencyRates1, currencyRates2));

    private final static String expectedResponse = String.format("[{\"symbol\":\"%s\",\"price\":\"%s\"},{\"symbol\":\"%s\",\"price\":\"%s\"}]",
            currencyRates1.getSymbol(), currencyRates1.getPrice(),
            currencyRates2.getSymbol(), currencyRates2.getPrice());

    public static String getTestJson() {
        return expectedResponse;
    }

    public static List<CurrencyRatesDTO> getTestArrayList() {
        return testArrayList;
    }
}