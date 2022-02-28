package ru.bandit.cryptobot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bandit.cryptobot.clients.BotAppAverageRatesClient;
import ru.bandit.cryptobot.dao.RatesArchiveDAO;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AverageRatesServiceTest {

    @MockBean
    RatesArchiveDAO ratesArchiveDAO;

    @MockBean
    RatesDAO ratesDAO;

    @MockBean
    BotAppAverageRatesClient botAppAverageRatesClient;

    AverageRatesService averageRatesService;

    @BeforeEach
    void SetUp() {
        averageRatesService = new AverageRatesService(botAppAverageRatesClient, ratesDAO, ratesArchiveDAO);
    }


    @Test
    void calculateNew1MinuteAverages() {

        //mocking all used classes
        when(ratesDAO.getCurrencyRates()).thenReturn(RatesDAOTestData.testCurrencyRatesMap1);
        when(ratesArchiveDAO.getCurrencyRatesArchive()).thenReturn(RatesDAOTestData.testArchiveRates);

        //simulating service method call
        Map<String, Double> calculatedAvg = averageRatesService.calculateNew1MinuteAverages();

        //checking that response is correct
        assertEquals(RatesDAOTestData.getTestAverageRates().get(RatesDAOTestData.testRateSymbol1),
                calculatedAvg.get(RatesDAOTestData.testRateSymbol1),
                0.0000001);
        assertEquals(RatesDAOTestData.getTestAverageRates().get(RatesDAOTestData.testRateSymbol2),
                calculatedAvg.get(RatesDAOTestData.testRateSymbol2),
                0.0000001);

        //verifying other methods interaction
        verify(ratesArchiveDAO, times(1)).appendNewRatesToArchive(RatesDAOTestData.testCurrencyRatesMap1);
        verify(ratesArchiveDAO, times(1)).getCurrencyRatesArchive();
        verify(ratesDAO, times(1)).getCurrencyRates();
    }
}