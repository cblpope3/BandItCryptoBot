package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.Set;

/**
 * Class that sends new rates to Bot-App api.
 */
@Service
public class BotAppCurrentRatesClient extends BotAppRatesClient {

    private final Logger logger = LoggerFactory.getLogger(BotAppCurrentRatesClient.class);

    @Autowired
    public BotAppCurrentRatesClient(RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.build());
    }

    /**
     * Post new currencies rates. Makes POST-request to Bot-App api.
     *
     * @param newRates {@link Set} <{@link CurrencyRatesDTO}> of current currency rates
     */
    @Override
    public void postNewRates(Set<CurrencyRatesDTO> newRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates", newRates, String.class);
            logger.trace("Rates have been sent to Bot App. Response status code is: {}", response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got exception while sending new rates to Bot-App: {}", e.getMessage());
        }
    }
}