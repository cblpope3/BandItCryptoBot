package ru.bandit.cryptobot.dao;

import org.junit.jupiter.api.Test;
import ru.bandit.cryptobot.test_data.CurrenciesTestData;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrentCurrencyRatesDAOTest {

    CurrentCurrencyRatesDAO currentCurrencyRatesDAO = new CurrentCurrencyRatesDAO();

    @Test
    void CurrencyRates() {
        currentCurrencyRatesDAO.setCurrencyRates(CurrenciesTestData.getTestCurrencyRates());

        assertEquals(CurrenciesTestData.getTestCurrencyRates(), currentCurrencyRatesDAO.getCurrencyRates());
        assertEquals(CurrenciesTestData.getCurrency1Value(),
                currentCurrencyRatesDAO.getRateBySymbol(CurrenciesTestData.getCurrencyPair1()));

        //FIXME This test can fail
        Timestamp lastUpdateTime = new Timestamp(System.currentTimeMillis());
        currentCurrencyRatesDAO.setLastUpdateTime();
        assertEquals(lastUpdateTime.toString(), currentCurrencyRatesDAO.getLastUpdateTime());
    }

}