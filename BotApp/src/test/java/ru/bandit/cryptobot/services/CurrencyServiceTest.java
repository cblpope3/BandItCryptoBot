package ru.bandit.cryptobot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bandit.cryptobot.dao.AverageCurrencyRatesDAO;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.CurrencyException;
import ru.bandit.cryptobot.repositories.CurrencyPairRepository;
import ru.bandit.cryptobot.repositories.CurrencyRepository;
import ru.bandit.cryptobot.test_data.CurrenciesTestData;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CurrencyServiceTest {

    @MockBean
    CurrencyRepository currencyRepository;

    @MockBean
    CurrencyPairRepository currencyPairRepository;

    @MockBean
    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    @MockBean
    AverageCurrencyRatesDAO averageCurrencyRatesDAO;

    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository,
                currencyPairRepository,
                currentCurrencyRatesDAO,
                averageCurrencyRatesDAO);
    }

    @Test
    void getCurrencyBySymbol() throws CommonBotAppException {

        //mocking all used classes
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyBTCSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyBTC());

        //simulating service method call
        CurrencyEntity givenCurrency = currencyService.getCurrencyBySymbol(CurrenciesTestData.getCurrencyBTCSymbol());

        //checking that response is correct
        assertEquals(CurrenciesTestData.getCurrencyBTC(), givenCurrency);

        //verifying other methods interaction
        verify(currencyRepository, times(1))
                .findByCurrencyNameUser(CurrenciesTestData.getCurrencyBTCSymbol());
    }

    @Test
    void getAllCurrencies() throws CommonBotAppException {
        //mocking all used classes
        when(currencyRepository.findAll())
                .thenReturn(CurrenciesTestData.getCurrenciesList());

        //simulating service method call
        Set<CurrencyEntity> givenCurrency = currencyService.getAllCurrencies();

        //checking that response is correct
        assertEquals(CurrenciesTestData.getCurrenciesSet(), givenCurrency);

        //verifying other methods interaction
        verify(currencyRepository, times(1))
                .findAll();
    }

    //Testing fine currencies call
    @Test
    void getCurrencyPairSeparate() throws CommonBotAppException {
        //mocking all used classes
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyBTCSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyBTC());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyETHSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyETH());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyEURSymbol()))
                .thenReturn(null);
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyRUBSymbol()))
                .thenReturn(null);

        when(currencyPairRepository.findByCurrency1(CurrenciesTestData.getCurrencyETH()))
                .thenReturn(List.of(CurrenciesTestData.getCurrencyPairETHRUB(), CurrenciesTestData.getCurrencyPairETHEUR()));
        when(currencyPairRepository.findByCurrency2(CurrenciesTestData.getCurrencyETH()))
                .thenReturn(List.of(CurrenciesTestData.getCurrencyPairBTCETH()));
        when(currencyPairRepository.findByCurrency1(CurrenciesTestData.getCurrencyBTC()))
                .thenReturn(List.of(CurrenciesTestData.getCurrencyPairBTCEUR(), CurrenciesTestData.getCurrencyPairBTCRUB(),
                        CurrenciesTestData.getCurrencyPairBTCETH()));
        when(currencyPairRepository.findByCurrency2(CurrenciesTestData.getCurrencyBTC()))
                .thenReturn(null);

        //simulating service method call
        CurrencyPairEntity givenCurrencyPair1 = currencyService.getCurrencyPair(CurrenciesTestData.getCurrencyBTCSymbol(), CurrenciesTestData.getCurrencyETHSymbol());
        CurrencyPairEntity givenCurrencyPair2 = currencyService.getCurrencyPair(CurrenciesTestData.getCurrencyETHSymbol(), CurrenciesTestData.getCurrencyBTCSymbol());

        //checking that response is correct
        assertEquals(CurrenciesTestData.getCurrencyPairBTCETH(), givenCurrencyPair1);
        assertEquals(CurrenciesTestData.getCurrencyPairBTCETH(), givenCurrencyPair2);

        //verifying other methods interaction
        verify(currencyRepository, times(4)).findByCurrencyNameUser(any());
        verify(currencyPairRepository, times(2)).findByCurrency1(any());
        verify(currencyPairRepository, times(2)).findByCurrency2(any());
    }

    //Testing if currency not found
    @Test
    void getCurrencyPairSeparateNoCurrency() {
        //mocking all used classes
        //currencies BTC and EUR are known
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyBTCSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyBTC());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyETHSymbol()))
                .thenReturn(null);
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyEURSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyEUR());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyRUBSymbol()))
                .thenReturn(null);


        int exceptionsNumber = 0;
        //simulating service method call
        try {
            //try to get known BTC and unknown EUR
            currencyService.getCurrencyPair(CurrenciesTestData.getCurrencyBTCSymbol(), CurrenciesTestData.getCurrencyETHSymbol());
        } catch (CommonBotAppException e) {
            ++exceptionsNumber;
            //todo decide if this assertion is fine
            assertEquals(CurrencyException.ExceptionCause.NO_CURRENCY.getMessage(), e.getUserFriendlyMessage());
        }

        //expecting 2 calls to findByCurrencyNameUser method
        verify(currencyRepository, times(2)).findByCurrencyNameUser(any());

        try {
            //try to get unknown RUB and known BTC
            currencyService.getCurrencyPair(CurrenciesTestData.getCurrencyRUBSymbol(), CurrenciesTestData.getCurrencyBTCSymbol());
        } catch (CommonBotAppException e) {
            ++exceptionsNumber;
        }

        //expecting 1 more call to findByCurrencyNameUser method (3 with previous)
        verify(currencyRepository, times(3)).findByCurrencyNameUser(any());

        //checking that response is correct
        assertEquals(2, exceptionsNumber);
    }

    //Testing if currency pair not found
    @Test
    void getCurrencyPairSeparateNoCurrencyPair() {
        //mocking all used classes
        //currencies BTC and EUR are known
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyBTCSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyBTC());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyETHSymbol()))
                .thenReturn(null);
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyEURSymbol()))
                .thenReturn(CurrenciesTestData.getCurrencyEUR());
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrencyRUBSymbol()))
                .thenReturn(null);

        int exceptionsCounter = 0;

        //simulating service method call
        CurrencyPairEntity givenCurrencyPair1 = null;
        try {
            givenCurrencyPair1 = currencyService.getCurrencyPair(CurrenciesTestData.getCurrencyBTCSymbol(), CurrenciesTestData.getCurrencyEURSymbol());
        } catch (CommonBotAppException e) {
            //todo need to check what kind of exception had happened
            ++exceptionsCounter;
        }

        //checking that response is correct
        assertNull(givenCurrencyPair1);
        assertEquals(1, exceptionsCounter);

        //verifying other methods interaction
        verify(currencyRepository, times(2)).findByCurrencyNameUser(any());
    }
}