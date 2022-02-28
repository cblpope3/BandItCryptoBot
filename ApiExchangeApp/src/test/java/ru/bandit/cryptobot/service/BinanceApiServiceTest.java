package ru.bandit.cryptobot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.clients.BinanceApiClient;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BinanceApiServiceTest {

    @MockBean
    BinanceApiClient binanceApiClient;

    @MockBean
    RatesDAO ratesDAO;

    BinanceApiService binanceApiService;

    @BeforeEach
    void setUp() {
        binanceApiService = new BinanceApiService(binanceApiClient, ratesDAO);
    }

    //testing fine method request
    @Test
    void getNewCurrencyRates() throws InterruptedException {

        //mocking all used classes
        when(binanceApiClient.getAllCurrencyPrices()).thenReturn(RatesDAOTestData.getUnfilteredBinanceData());

        //simulating service method call
        binanceApiService.getNewCurrencyRates();

        //checking that response is correct
        verify(ratesDAO, times(1)).setCurrencyRates(RatesDAOTestData.getFilteredBinanceData());

        //verifying other methods interaction
        verify(binanceApiClient, times(1)).getAllCurrencyPrices();
    }

    //testing method request if got TOO_MANY_REQUESTS status code
    @Test
    @Disabled("Take too much time!")
    void getNewCurrencyRatesCoolDown() throws InterruptedException {

        //mocking all used classes
        when(binanceApiClient.getAllCurrencyPrices())
                .thenThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS));

        long methodStart = System.currentTimeMillis();

        //simulating service method call
        binanceApiService.getNewCurrencyRates();

        long methodStop = System.currentTimeMillis();
        long methodDuration = methodStop - methodStart;
        System.out.println("Cooldown timer duration: " + methodDuration);
        assertTrue(methodDuration > 50000);
    }

    //testing method request if got I_M_TEAPOT status code
    @Test
    @Disabled("This test terminates process.")
    void getNewCurrencyRatesBanned() throws InterruptedException {

        //mocking all used classes
        when(binanceApiClient.getAllCurrencyPrices())
                .thenThrow(new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT));

        //simulating service method call
        binanceApiService.getNewCurrencyRates();

    }
}