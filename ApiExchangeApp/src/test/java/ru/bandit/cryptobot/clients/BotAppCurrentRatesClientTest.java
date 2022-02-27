package ru.bandit.cryptobot.clients;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@RestClientTest(BotAppCurrentRatesClient.class)
class BotAppCurrentRatesClientTest {

    @Value("${bot-app.hostname}")
    String botAppCurrencyUrl;

    @Autowired
    BotAppCurrentRatesClient botAppCurrentRatesClient;

    @Autowired
    MockRestServiceServer server;

    @Test
    void postNewRates() {

        this.server.expect(requestTo(botAppCurrencyUrl + "rates"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(ClientsTestData.getTestJson()))
                .andRespond(withStatus(HttpStatus.ACCEPTED));

        botAppCurrentRatesClient.postNewRates(ClientsTestData.getTestArrayList());
    }

    @Test
    void postNewRatesException() {

        this.server.expect(requestTo(botAppCurrencyUrl + "rates"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(ClientsTestData.getTestJson()))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        botAppCurrentRatesClient.postNewRates(ClientsTestData.getTestArrayList());
    }
}