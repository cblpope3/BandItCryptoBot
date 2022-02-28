package ru.bandit.cryptobot;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.bandit.cryptobot.clients.BinanceApiClient;

@RunWith(MockitoJUnitRunner.class)
public class ApiManagerCoolDownTest extends TestCase {

    @Mock
    BinanceApiClient mainServiceMock;

    @Test
    @Ignore("Take too much time!")
    public void testCoolDownTimer() throws InterruptedException {

//        ApiManager apiManager = new ApiManager(mainServiceMock, reserveServiceMock);
//        Map<String, Double> mainApiResponse = new HashMap<>();
//        Map<String, Double> reserveApiResponse = new HashMap<>();
//
//        mainApiResponse.put("main", 12.99);
//        reserveApiResponse.put("reserve", 11.99);
//
//        when(mainServiceMock.getAllCurrencyPrices()).thenReturn(mainApiResponse);
//        when(reserveServiceMock.getAllCurrencyPrices()).thenReturn(reserveApiResponse);
//
//        assertEquals("{main=12.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());
//
//        apiManager.switchToReserveApi();
//        assertEquals("{reserve=11.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());
//
//        TimeUnit.SECONDS.sleep(30);
//        assertEquals("{reserve=11.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());
//
//        //wait
//        TimeUnit.SECONDS.sleep(40);
//        assertEquals("{main=12.99}", apiManager.getCurrentApi().getAllCurrencyPrices().toString());

    }
}