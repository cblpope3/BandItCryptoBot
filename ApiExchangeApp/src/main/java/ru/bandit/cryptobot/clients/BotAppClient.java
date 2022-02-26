package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class that interacts with Bot-App api.
 */
@Service
public class BotAppClient {

    private final RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger(BotAppClient.class);

    @Value("${bot-app.hostname}")
    private String botAppCurrencyUrl;

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
     * Request all active triggers from Bot-App api.
     *
     * @return {@link List} of available triggers ({@link TriggerDTO}).
     */
    public List<TriggerDTO> getAllTriggers() {

        ResponseEntity<List<TriggerDTO>> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(botAppCurrencyUrl + "trigger/getAllTarget", HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            logger.error("Error while requesting all triggers list from Bot-App: {}", e.getMessage());
        }

        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.trace("No active triggers received from Bot-App.");
            return Collections.emptyList();
        } else {
            logger.trace("Triggers list requested from Bot App. Response status is: {}", responseEntity.getStatusCode());
            return responseEntity.getBody();
        }
    }

    /**
     * Send worked trigger to Bot-App api.
     *
     * @param triggerId id of worked trigger.
     * @param value     value of trigger-watched currency rate.
     */
    public void postWorkedTrigger(Long triggerId, Double value) {

        String requestUrl = String.format("%strigger/worked?triggerId=%d&value=%f", botAppCurrencyUrl, triggerId, value);

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(requestUrl, null, String.class);
            logger.trace("Worked trigger #{} is sent to Bot App. Response status code is {}.", triggerId, response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Error while trying to send worked trigger to Bot-App: {}", e.getMessage());
        }
    }

    /**
     * Send {@link Map} of worked triggers to Bot-App
     *
     * @param triggersCollection {@link Map} of trigger id ({@link Long}) -> currency rate ({@link Double})
     */
    public void postWorkedTriggersCollection(Map<Long, Double> triggersCollection) {

        if (triggersCollection == null || triggersCollection.isEmpty()) return;

        logger.trace("Posting worked triggers collection: {}", triggersCollection);

        for (Map.Entry<Long, Double> workedTrigger : triggersCollection.entrySet()) {
            this.postWorkedTrigger(workedTrigger.getKey(), workedTrigger.getValue());
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