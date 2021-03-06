package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.Collections;
import java.util.List;

/**
 * Class that interacts with remote currency rates api (binance.com).
 */
@Service
public class BinanceApiClient {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(BinanceApiClient.class);

    @Autowired
    public BinanceApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new BinanceApiErrorHandler())
                .build();
    }

    /**
     * Get latest currency rates
     *
     * @return {@link List} <{@link CurrencyRatesDTO}> - latest currency rates.
     * @throws ResponseStatusException in case of too frequent requests.
     */
    public List<CurrencyRatesDTO> getAllCurrencyPrices() throws ResponseStatusException {

        ResponseEntity<List<CurrencyRatesDTO>> responseEntity = null;

        //trying to get new data
        try {
            responseEntity = restTemplate.exchange(
                    "https://api.binance.com/api/v3/ticker/price", HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            logger.trace("Got response from Binance api. Status code is: {}", responseEntity.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got error while trying request data from binance.com: {}", e.getMessage());
        }


        if (responseEntity == null || responseEntity.getBody() == null) {

            //handle empty body response
            logger.error("Got empty response from Binance.com");
            return Collections.emptyList();

        } else {
            //hope that other possible statuses are okay
            return responseEntity.getBody();
        }
    }
}