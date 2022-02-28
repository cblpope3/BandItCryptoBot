package ru.bandit.cryptobot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.bandit.cryptobot.clients.BotAppTriggersClient;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.dao.TriggersDAO;
import ru.bandit.cryptobot.test_data.RatesDAOTestData;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TriggersServiceTest {

    TriggersService triggersService;

    @MockBean
    TriggersDAO triggersDAO;

    @MockBean
    BotAppTriggersClient botAppTriggersClient;

    @MockBean
    RatesDAO ratesDAO;

    @BeforeEach
    void setUp() {
        triggersService = new TriggersService(triggersDAO, botAppTriggersClient, ratesDAO);
    }

    //testing fine check triggers method call
    @Test
    void checkTriggers() {
        //mocking all used classes
        when(triggersDAO.getTriggersList()).thenReturn(RatesDAOTestData.testTriggersList);
        when(ratesDAO.getCurrencyRates()).thenReturn(RatesDAOTestData.testCurrencyRatesMap1);

        //simulating service method call
        Map<Long, Double> workedTriggersList = triggersService.checkTriggers();

        //checking that response is correct
        assertEquals(RatesDAOTestData.workedTriggersMap, workedTriggersList);

        //verifying other methods interaction
        verify(triggersDAO).deleteTrigger(RatesDAOTestData.testTriggerWorked1.getId());
        verify(triggersDAO).deleteTrigger(RatesDAOTestData.testTriggerWorked1.getId());
        verify(triggersDAO, times(2)).deleteTrigger(any());
        verify(triggersDAO, times(1)).getTriggersList();
        verify(ratesDAO, times(1)).getCurrencyRates();
    }

    //testing check triggers method call with null triggers list
    @Test
    void checkTriggersNull() {
        //mocking all used classes
        when(triggersDAO.getTriggersList()).thenReturn(null);
        when(ratesDAO.getCurrencyRates()).thenReturn(RatesDAOTestData.testCurrencyRatesMap1);

        //simulating service method call
        Map<Long, Double> workedTriggersList = triggersService.checkTriggers();

        //checking that response is correct
        assertEquals(Collections.emptyMap(), workedTriggersList);

        //verifying other methods interaction
        verify(triggersDAO, times(0)).deleteTrigger(any());
        verify(triggersDAO, times(1)).getTriggersList();
        verify(ratesDAO, times(1)).getCurrencyRates();
    }

    //testing check triggers method call with empty triggers list
    @Test
    void checkTriggersEmpty() {
        //mocking all used classes
        when(triggersDAO.getTriggersList()).thenReturn(Collections.emptyList());
        when(ratesDAO.getCurrencyRates()).thenReturn(RatesDAOTestData.testCurrencyRatesMap1);

        //simulating service method call
        Map<Long, Double> workedTriggersList = triggersService.checkTriggers();

        //checking that response is correct
        assertEquals(Collections.emptyMap(), workedTriggersList);

        //verifying other methods interaction
        verify(triggersDAO, times(0)).deleteTrigger(any());
        verify(triggersDAO, times(1)).getTriggersList();
        verify(ratesDAO, times(1)).getCurrencyRates();
    }

    @Test
    void postWorkedTriggersCollection() {

        //simulating service method call
        triggersService.postWorkedTriggersCollection(RatesDAOTestData.workedTriggersMap);
        //simulating service method call if triggers map is null
        triggersService.postWorkedTriggersCollection(null);
        //simulating service method call if triggers map is empty
        triggersService.postWorkedTriggersCollection(Collections.emptyMap());

        //verifying other methods interaction
        verify(botAppTriggersClient, times(1)).postWorkedTrigger(RatesDAOTestData.testTriggerWorked1.getId(),
                RatesDAOTestData.workedTriggersMap.get(RatesDAOTestData.testTriggerWorked1.getId()));
        verify(botAppTriggersClient, times(1)).postWorkedTrigger(RatesDAOTestData.testTriggerWorked2.getId(),
                RatesDAOTestData.workedTriggersMap.get(RatesDAOTestData.testTriggerWorked2.getId()));
        verify(botAppTriggersClient, times(2)).postWorkedTrigger(any(), any());
    }

    @Test
    void addTrigger() {
        //simulating service method call
        triggersService.addTrigger(RatesDAOTestData.testTrigger1);

        //verifying other methods interaction
        verify(triggersDAO, times(1)).addTrigger(RatesDAOTestData.testTrigger1);
    }

    @Test
    void deleteTrigger() {
        //simulating service method call
        triggersService.deleteTrigger(RatesDAOTestData.testTrigger1.getId());

        //verifying other methods interaction
        verify(triggersDAO, times(1)).deleteTrigger(RatesDAOTestData.testTrigger1.getId());
    }

    @Test
    void updateTriggerList() {
        //mocking all used classes
        when(botAppTriggersClient.getAllTriggers()).thenReturn(RatesDAOTestData.testTriggersList);

        //simulating service method call
        triggersService.updateTriggerList();

        //verifying other methods interaction
        verify(triggersDAO, times(1)).setTriggersList(RatesDAOTestData.testTriggersList);
    }
}