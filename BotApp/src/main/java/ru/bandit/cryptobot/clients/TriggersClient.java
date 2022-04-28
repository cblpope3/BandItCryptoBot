package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.dto.TriggerDTO;

/**
 * Class that exchanges triggers information with Triggers-App
 */
@Service
public class TriggersClient {

    private final Logger logger = LoggerFactory.getLogger(TriggersClient.class);

    private final RestTemplate restTemplate;

    @SuppressWarnings("unused")
    @Value("${api-app.hostname}")
    private String triggerAppCurrencyUrl;

    @Autowired
    public TriggersClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new TriggersClientErrorHandler())
                .build();
    }

    /**
     * Post new trigger to Triggers-App
     *
     * @param triggerDTO trigger data-transfer object ({@link TriggerDTO}) represents trigger to be processed.
     * @return true if addition is successful, false if not.
     */
    public boolean addNewTrigger(TriggerDTO triggerDTO) {

        if (triggerDTO == null) {
            logger.error("Trying to send null trigger to Trigger-App.");
            return false;
        }

        ResponseEntity<Object> response;
        try {

            response = restTemplate.postForEntity(triggerAppCurrencyUrl + "trigger", triggerDTO, Object.class);
            logger.trace("Trigger have been sent to Trigger App. Response status code is: {}", response.getStatusCode());

        } catch (ResponseStatusException e) {

            logger.error("Got exception while sending new trigger to Trigger-App: {}", e.getMessage());
            return false;
        } catch (RestClientException e) {
            logger.error("Got exception. Problem with Trigger-App connection: {}.", e.getMessage());
            return false;
        }

        if (response.getStatusCode() == HttpStatus.ACCEPTED) {
            if (logger.isTraceEnabled())
                logger.trace("New trigger have been sent successfully: {}", triggerDTO);
            return true;
        } else {
            if (logger.isErrorEnabled())
                logger.error("Unexpected response while sending new trigger to Trigger-App: {}", triggerDTO);
            return false;
        }
    }

    /**
     * Delete trigger from Triggers-App.
     *
     * @param triggerId id of trigger to be deleted ({@link Long}).
     * @return true if delete is successful. False otherwise.
     */
    public boolean deleteTrigger(Long triggerId) {
        ResponseEntity<Object> response = null;

        try {
            response = restTemplate.exchange(triggerAppCurrencyUrl + "trigger/" + triggerId.toString(),
                    HttpMethod.DELETE,
                    null,
                    Object.class);

        } catch (ResponseStatusException e) {
            logger.warn("Exception during sending 'delete trigger #{}' request to api-app by address {}trigger/{}: {}",
                    triggerId,
                    triggerAppCurrencyUrl,
                    triggerId,
                    e.getMessage());
        } catch (RestClientException e) {
            logger.error("Got exception. Problem with Trigger-App connection: {}.", e.getMessage());
            return false;
        }

        if (response == null) {
            logger.error("Error while sending delete request to Trigger-App: response is null.");
            return false;
        } else if (response.getStatusCode() == HttpStatus.OK) {
            logger.trace("Request to delete trigger #{} processed successfully.", triggerId);
            return true;
        } else {
            //unreachable statement
            logger.error("Unknown status code: {}.", response.getStatusCode());
            return false;
        }
    }
}
