package ru.bandit.cryptobot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.repositories.CurrencyRepository;
import ru.bandit.cryptobot.test_data.CurrenciesTestData;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CurrencyServiceTest {

    @MockBean
    CurrencyRepository currencyRepository;

    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository);
    }

    @Test
    void getCurrencyBySymbol() {

        //mocking all used classes
        when(currencyRepository.findByCurrencyNameUser(CurrenciesTestData.getCurrency1Symbol()))
                .thenReturn(CurrenciesTestData.getCurrency1());

        //simulating service method call
        CurrencyEntity givenCurrency = currencyService.getCurrencyBySymbol(CurrenciesTestData.getCurrency1Symbol());

        //checking that response is correct
        assertEquals(CurrenciesTestData.getCurrency1(), givenCurrency);

        //verifying other methods interaction
        verify(currencyRepository, times(1))
                .findByCurrencyNameUser(CurrenciesTestData.getCurrency1Symbol());
    }

    @Test
    void getAllCurrencies() {
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
}