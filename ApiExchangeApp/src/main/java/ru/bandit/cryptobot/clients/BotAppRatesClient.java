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

import java.util.ArrayList;
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
    public void postNewRates(Map<String, Double> newRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates", convertCurrencyRatesToList(newRates), String.class);
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
    public void postAverageRates(Map<String, Double> averageRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates/1m_avg", convertCurrencyRatesToList(averageRates), String.class);
            logger.trace("Average values have been sent to Bot App. Response code is: {}", response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got exception while sending new average values to Bot-App: {}", e.getMessage());
        }
    }

    /**
     * Converts data from {@link Map} <{@link String}, {@link Double}> format to {@link List}<{@link CurrencyRatesDTO}>.
     *
     * @param input convertable {@link Map} of currency rates
     * @return {@link List} of {@link CurrencyRatesDTO}
     */
    private List<CurrencyRatesDTO> convertCurrencyRatesToList(Map<String, Double> input) {
        List<CurrencyRatesDTO> result = new ArrayList<>();

        for (Map.Entry<String, Double> rate : input.entrySet()) {
            result.add(new CurrencyRatesDTO(rate.getKey(), rate.getValue().toString()));
        }

        return result;
    }
}