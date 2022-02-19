package ru.bandit.cryptobot.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestServiceTest extends TestCase {

    private final TestService testService = new TestService();

    @Test
    public void testGetTestMessage() {
        assertEquals("OK", testService.getTestMessage(null));
        assertEquals("hello", testService.getTestMessage("hello"));
    }
}