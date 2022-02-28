package ru.bandit.cryptobot.clients;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.test_data.ClientsTestData;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(BinanceApiClient.class)
class BinanceApiClientTest {

    private final String requestURI = "https://api.binance.com/api/v3/ticker/price";

    @Autowired
    BinanceApiClient binanceApiClient;

    @Autowired
    MockRestServiceServer server;

    //test fine response
    @Test
    void getAllCurrencyPrices() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(ClientsTestData.getTestJson(), MediaType.APPLICATION_JSON));

        assertEquals(ClientsTestData.getTestArrayList(), binanceApiClient.getAllCurrencyPrices());
    }

    //test empty response
    @Test
    void getAllCurrencyPricesEmptyResponse() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        assertEquals(Collections.emptyList(), binanceApiClient.getAllCurrencyPrices());
    }

    //test if binance.com is not responding
    @Test
    void getAllCurrencyPricesException() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(response -> {
                    throw new RestClientException("Test exception");
                });

        assertEquals(Collections.emptyList(), binanceApiClient.getAllCurrencyPrices());
    }

    //test if binance warn that there is too many request
    @Test
    void getAllCurrencyPricesWarn() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        boolean exceptionHappened = false;

        try {
            binanceApiClient.getAllCurrencyPrices();
        } catch (ResponseStatusException e) {
            if (e.getStatus() == HttpStatus.TOO_MANY_REQUESTS) exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    //test if binance has banned us
    @Test
    void getAllCurrencyPricesBanned() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.I_AM_A_TEAPOT));

        boolean exceptionHappened = false;

        try {
            binanceApiClient.getAllCurrencyPrices();
        } catch (ResponseStatusException e) {
            if (e.getStatus() == HttpStatus.I_AM_A_TEAPOT) exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    //test if unknown status code have been responded
    @Test
    void getAllCurrencyPricesUnknownError() {
        this.server.expect(requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        boolean exceptionHappened = false;

        try {
            binanceApiClient.getAllCurrencyPrices();
        } catch (ResponseStatusException e) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }
}