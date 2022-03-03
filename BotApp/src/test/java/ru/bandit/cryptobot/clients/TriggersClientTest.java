package ru.bandit.cryptobot.clients;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import ru.bandit.cryptobot.test_data.TriggersTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@RestClientTest(TriggersClient.class)
class TriggersClientTest {

    @Value("${api-app.hostname}")
    String triggerAppUrl;

    @Autowired
    TriggersClient triggersClient;

    @Autowired
    MockRestServiceServer server;

    //testing fine add new trigger request
    @Test
    void addNewTrigger() {
        String expectedRequest = String.format("%strigger", triggerAppUrl);

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(TriggersTestData.getTrigger().toJson()))
                .andRespond(withStatus(HttpStatus.ACCEPTED));

        assertTrue(triggersClient.addNewTrigger(TriggersTestData.getTrigger()));
    }

    //testing null add new trigger request
    @Test
    void addNewTriggerNullRequest() {

        String expectedRequest = String.format("%strigger", triggerAppUrl);

        this.server.expect(ExpectedCount.never(), requestTo(expectedRequest));

        assertFalse(triggersClient.addNewTrigger(null));
    }

    //testing add new trigger request with no response
    @Test
    void addNewTriggerNullResponse() {
        String expectedRequest = String.format("%strigger", triggerAppUrl);

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(TriggersTestData.getTrigger().toJson()))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        assertFalse(triggersClient.addNewTrigger(TriggersTestData.getTrigger()));
    }

    //testing add new trigger request with bad response
    @Test
    void addNewTriggerBadResponse() {
        String expectedRequest = String.format("%strigger", triggerAppUrl);

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(TriggersTestData.getTrigger().toJson()))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertFalse(triggersClient.addNewTrigger(TriggersTestData.getTrigger()));
    }

    //testing add new trigger request with unexpected response
    @Test
    void addNewTriggerUnexpectedResponse() {
        String expectedRequest = String.format("%strigger", triggerAppUrl);

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(TriggersTestData.getTrigger().toJson()))
                .andRespond(withStatus(HttpStatus.ALREADY_REPORTED));

        assertFalse(triggersClient.addNewTrigger(TriggersTestData.getTrigger()));
    }

    //testing fine delete trigger request
    @Test
    void deleteTrigger() {
        String expectedRequest = String.format("%strigger/%d", triggerAppUrl, TriggersTestData.getTrigger().getId());

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        assertTrue(triggersClient.deleteTrigger(TriggersTestData.getTrigger().getId()));
    }

    //testing delete trigger request with no response
    @Test
    void deleteTriggerNoResponse() {
        String expectedRequest = String.format("%strigger/%d", triggerAppUrl, TriggersTestData.getTrigger().getId());

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        assertFalse(triggersClient.deleteTrigger(TriggersTestData.getTrigger().getId()));
    }

    //testing delete trigger request if trigger is not found
    @Test
    void deleteTriggerNotFound() {
        String expectedRequest = String.format("%strigger/%d", triggerAppUrl, TriggersTestData.getTrigger().getId());

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertFalse(triggersClient.deleteTrigger(TriggersTestData.getTrigger().getId()));
    }

    //testing unknown response while sending delete trigger request
    @Test
    void deleteTriggerUnknown() {
        String expectedRequest = String.format("%strigger/%d", triggerAppUrl, TriggersTestData.getTrigger().getId());

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.ALREADY_REPORTED));

        assertFalse(triggersClient.deleteTrigger(TriggersTestData.getTrigger().getId()));
    }

    //testing unknown response while sending delete trigger request
    @Test
    void deleteTriggerUnknown2() {
        String expectedRequest = String.format("%strigger/%d", triggerAppUrl, TriggersTestData.getTrigger().getId());

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertFalse(triggersClient.deleteTrigger(TriggersTestData.getTrigger().getId()));
    }
}