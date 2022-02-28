package ru.bandit.cryptobot.clients;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.test_data.ClientsTestData;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(BotAppTriggersClient.class)
class BotAppTriggersClientTest {

    @Value("${bot-app.hostname}")
    String botAppCurrencyUrl;

    @Autowired
    BotAppTriggersClient botAppTriggersClient;

    @Autowired
    MockRestServiceServer server;

    //testing fine triggers request - response
    @Test
    void getAllTriggers() {
        String expectedRequest = String.format("%strigger/getAllTarget", botAppCurrencyUrl);

        List<TriggerDTO> expectedList = List.of(ClientsTestData.testTrigger1, ClientsTestData.testTrigger2);

        this.server.expect(once(), requestTo(expectedRequest))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ClientsTestData.getTriggersJson(), MediaType.APPLICATION_JSON));

        assertEquals(expectedList, botAppTriggersClient.getAllTriggers());
    }

    //testing if response is empty
    @Test
    void getAllTriggersEmptyResponse() {
        String expectedRequest = String.format("%strigger/getAllTarget", botAppCurrencyUrl);

        this.server.expect(requestTo(expectedRequest))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        assertEquals(Collections.emptyList(), botAppTriggersClient.getAllTriggers());
    }

    //testing if no response
    @Test
    void getAllTriggersNoResponse() {
        String expectedRequest = String.format("%strigger/getAllTarget", botAppCurrencyUrl);

        this.server.expect(requestTo(expectedRequest))
                .andExpect(method(HttpMethod.GET))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        assertEquals(Collections.emptyList(), botAppTriggersClient.getAllTriggers());
    }

    //testing fine trigger post
    @Test
    void postWorkedTrigger() {
        String expectedRequest = String.format("%strigger/worked?triggerId=%d&value=%f", botAppCurrencyUrl,
                ClientsTestData.trigger1Id, ClientsTestData.trigger1Value);

        //todo deal with posted params
        this.server.expect(requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.ACCEPTED));

        botAppTriggersClient.postWorkedTrigger(ClientsTestData.trigger1Id, ClientsTestData.trigger1Value);
    }

    //testing trigger post with no answer
    @Test
    void postWorkedTriggerNoAnswer() {
        String expectedRequest = String.format("%strigger/worked?triggerId=%d&value=%f", botAppCurrencyUrl,
                ClientsTestData.trigger1Id, ClientsTestData.trigger1Value);

        this.server.expect(requestTo(expectedRequest))
                .andExpect(method(HttpMethod.POST))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        botAppTriggersClient.postWorkedTrigger(ClientsTestData.trigger1Id, ClientsTestData.trigger1Value);
    }

    //testing null arguments
    @Test
    void postWorkedTriggerNoParams() {

        boolean exceptionHappened = false;

        try {
            botAppTriggersClient.postWorkedTrigger(ClientsTestData.trigger1Id, null);
        } catch (NullPointerException e) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);

        exceptionHappened = false;

        try {
            botAppTriggersClient.postWorkedTrigger(null, ClientsTestData.trigger1Value);
        } catch (NullPointerException e) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);

        exceptionHappened = false;

        try {
            botAppTriggersClient.postWorkedTrigger(null, null);
        } catch (NullPointerException e) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);

    }
}