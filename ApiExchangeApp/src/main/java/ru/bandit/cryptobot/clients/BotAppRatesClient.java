package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;
import java.util.Map;

/**
 * Class that sends new rates to Bot-App api.
 */
@Service
public class BotAppRatesClient {

    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(BotAppRatesClient.class);

    @Value("${bot-app.hostname}")
    private String botAppCurrencyUrl;

    @Autowired
    public BotAppRatesClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    /**
     * Post new currencies rates. Makes POST-request to Bot-App api.
     *
     * @param newRates {@link Map} <{@link String}><{@link Double}> of current currency rates
     */
    public void postNewRates(List<CurrencyRatesDTO> newRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates", newRates, String.class);
            logger.trace("Rates have been sent to Bot App. Response status code is: {}", response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got exception while sending new rates to Bot-App: {}", e.getMessage());
        }
    }

    /**
     * Post new 1-minute average currencies rates. Makes POST-request to Bot-App api.
     *
     * @param averageRates {@link Map} <{@link String}, {@link Double}> of current 1-minute average currency rates
     */
    public void postAverageRates(List<CurrencyRatesDTO> averageRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates/1m_avg", averageRates, String.class);
            logger.trace("Average values have been sent to Bot App. Response code is: {}", response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got exception while sending new average values to Bot-App: {}", e.getMessage());
        }
    }
}