package ru.bandit.cryptobot;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.bandit.cryptobot.service.BinanceApiService;
import ru.bandit.cryptobot.service.CoinGeckoApiService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiManagerTest extends TestCase {

    @Mock
    BinanceApiService mainServiceMock;

    @Mock
    CoinGeckoApiService reserveServiceMock;

    @Test
    public void testApiManager() {

        ApiManager apiManager = new ApiManager(mainServiceMock, reserveServiceMock);
        Map<String, Double> mainApiResponse = new HashMap<>();
        Map<String, Double> reserveApiResponse = new HashMap<>();

        mainApiResponse.put("main", 12.99);
        reserveApiResponse.put("reserve", 11.99);

        when(mainServiceMock.getAllCurrencyPrices()).thenReturn(mainApiResponse);
        when(reserveServiceMock.getAllCurrencyPrices()).thenReturn(reserveApiResponse);

        assertEquals("{main=12.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());

        apiManager.switchToOtherApi();
        assertEquals("{reserve=11.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());

        apiManager.switchToOtherApi();
        assertEquals("{main=12.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());

        apiManager.switchToMainApi();
        assertEquals("{main=12.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());
    }

}