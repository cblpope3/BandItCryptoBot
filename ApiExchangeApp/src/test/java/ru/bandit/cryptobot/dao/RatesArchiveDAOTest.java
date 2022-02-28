package ru.bandit.cryptobot.dao;

import org.junit.jupiter.api.Test;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatesArchiveDAOTest {

    RatesArchiveDAO ratesArchiveDAO = new RatesArchiveDAO();

    @Test
    void getCurrencyRatesArchive() {
        ratesArchiveDAO.appendNewRatesToArchive(RatesDAOTestData.getFilteredBinanceData());

        assertEquals(List.of(RatesDAOTestData.getFilteredBinanceData()), ratesArchiveDAO.getCurrencyRatesArchive());
    }
}