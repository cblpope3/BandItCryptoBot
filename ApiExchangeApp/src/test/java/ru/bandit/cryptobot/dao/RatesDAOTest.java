package ru.bandit.cryptobot.dao;

import org.junit.jupiter.api.Test;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatesDAOTest {

    RatesDAO ratesDAO = new RatesDAO();

    @Test
    void getCurrencyRates() {
        ratesDAO.setCurrencyRates(RatesDAOTestData.getFilteredBinanceData());

        assertEquals(RatesDAOTestData.getFilteredBinanceData(), ratesDAO.getCurrencyRates());
    }
}