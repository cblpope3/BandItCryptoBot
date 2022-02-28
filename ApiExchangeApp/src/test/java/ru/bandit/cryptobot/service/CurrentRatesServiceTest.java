package ru.bandit.cryptobot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bandit.cryptobot.clients.BotAppCurrentRatesClient;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CurrentRatesServiceTest {

    @MockBean
    BotAppCurrentRatesClient botAppCurrentRatesClient;

    @MockBean
    RatesDAO ratesDAO;

    CurrentRatesService currentRatesService;

    @BeforeEach
    void setUp() {
        currentRatesService = new CurrentRatesService(botAppCurrentRatesClient, ratesDAO);
    }

    @Test
    void getCurrencyRates() {
        //mocking all used classes
        when(ratesDAO.getCurrencyRates()).thenReturn(RatesDAOTestData.testCurrencyRatesMap1);

        //simulating service method call
        Map<String, Double> givenRates = currentRatesService.getCurrencyRates();

        //checking that response is correct
        assertEquals(RatesDAOTestData.testCurrencyRatesMap1, givenRates);

        //verifying other methods interaction
        verify(ratesDAO, times(1)).getCurrencyRates();
    }

    @Test
    void setCurrencyRates() {

        //simulating service method call
        currentRatesService.setCurrencyRates(RatesDAOTestData.testCurrencyRatesMap1);

        //verifying other methods interaction
        verify(ratesDAO, times(1)).setCurrencyRates(RatesDAOTestData.testCurrencyRatesMap1);
    }

    //testing fine rates publishing
    @Test
    void publishNewRates() {

        //simulating service method call
        currentRatesService.publishNewRates(RatesDAOTestData.testCurrencyRatesMap1);

        //verifying other methods interaction
        verify(botAppCurrentRatesClient, times(1)).postNewRates(RatesDAOTestData.getCurrentRatesList());

    }

    //testing null rates publishing
    @Test
    void publishNewRatesNull() {

        //simulating service method call
        currentRatesService.publishNewRates(null);
        currentRatesService.publishNewRates(Collections.emptyMap());

        //verifying other methods interaction
        verify(botAppCurrentRatesClient, times(0)).postNewRates(any());

    }
}