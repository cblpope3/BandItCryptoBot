package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.Collections;
import java.util.List;

/**
 * Class that communicates with Bot-App api about triggers.
 */
@Service
public class BotAppTriggersClient {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(BotAppTriggersClient.class);

    @Value("${bot-app.hostname}")
    private String botAppCurrencyUrl;

    @Autowired
    public BotAppTriggersClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
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

        //check input params
        if (triggerId == null || value == null) {
            logger.error("Trying to send null in params. TriggerId = {}, Value = {}", triggerId, value);
            throw new NullPointerException("Arguments cannot be null!");
        }

        String requestUrl = String.format("%strigger/worked?triggerId=%d&value=%f", botAppCurrencyUrl, triggerId, value);

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(requestUrl, null, String.class);
            logger.trace("Worked trigger #{} is sent to Bot App. Response status code is {}.", triggerId, response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Error while trying to send worked trigger to Bot-App: {}", e.getMessage());
        }
    }
}