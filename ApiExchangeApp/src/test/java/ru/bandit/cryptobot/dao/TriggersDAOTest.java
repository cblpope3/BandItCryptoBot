package ru.bandit.cryptobot.dao;

import org.junit.jupiter.api.Test;
import ru.bandit.cryptobot.test_data.ClientsTestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriggersDAOTest {

    TriggersDAO triggersDAO = new TriggersDAO();

    @Test
    void getTriggersList() {
        triggersDAO.setTriggersList(List.of(ClientsTestData.testTrigger1, ClientsTestData.testTrigger2));

        assertEquals(List.of(ClientsTestData.testTrigger1, ClientsTestData.testTrigger2),
                triggersDAO.getTriggersList());

        triggersDAO.addTrigger(ClientsTestData.testTriggerWorked1);

        assertEquals(List.of(ClientsTestData.testTrigger1,
                ClientsTestData.testTrigger2,
                ClientsTestData.testTriggerWorked1), triggersDAO.getTriggersList());

        triggersDAO.deleteTrigger(ClientsTestData.testTrigger1.getId());

        assertEquals(List.of(ClientsTestData.testTrigger2,
                ClientsTestData.testTriggerWorked1), triggersDAO.getTriggersList());
    }
}