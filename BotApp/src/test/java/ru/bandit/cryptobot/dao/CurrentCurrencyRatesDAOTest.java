package ru.bandit.cryptobot.dao;

import org.junit.jupiter.api.Test;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.test_data.CurrenciesTestData;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrentCurrencyRatesDAOTest {

    CurrentCurrencyRatesDAO currentCurrencyRatesDAO = new CurrentCurrencyRatesDAO();

    @Test
    void CurrencyRates() throws CommonBotAppException {
        currentCurrencyRatesDAO.setCurrencyRates(CurrenciesTestData.getTestCurrencyRates());

        assertEquals(CurrenciesTestData.getTestCurrencyRates(), currentCurrencyRatesDAO.getCurrencyRates());
        assertEquals(CurrenciesTestData.getCurrencyPairBTCEURValue(),
                currentCurrencyRatesDAO.getRateBySymbol(CurrenciesTestData.getCurrencyPairBTCEURSymbol()));

        //FIXME This test can fail
        Timestamp lastUpdateTime = new Timestamp(System.currentTimeMillis());
        currentCurrencyRatesDAO.setLastUpdateTime();
        assertEquals(lastUpdateTime.toString(), currentCurrencyRatesDAO.getLastUpdateTime());
    }

}