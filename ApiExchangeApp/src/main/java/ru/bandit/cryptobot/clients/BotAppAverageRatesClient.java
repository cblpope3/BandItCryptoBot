package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;
import java.util.Map;

/**
 * Class that sends new rates to Bot-App api.
 */
@Service
public class BotAppAverageRatesClient extends BotAppRatesClient {

    private final Logger logger = LoggerFactory.getLogger(BotAppAverageRatesClient.class);

    @Autowired
    public BotAppAverageRatesClient(RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.build());
    }

    /**
     * Post new 1-minute average currencies rates. Makes POST-request to Bot-App api.
     *
     * @param averageRates {@link Map} <{@link String}, {@link Double}> of current 1-minute average currency rates
     */
    @Override
    public void postNewRates(List<CurrencyRatesDTO> averageRates) {

        try {
            ResponseEntity<String> response;
            response = restTemplate.postForEntity(botAppCurrencyUrl + "rates/1m_avg", averageRates, String.class);
            logger.trace("Average values have been sent to Bot App. Response code is: {}", response.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Got exception while sending new average values to Bot-App: {}", e.getMessage());
        }
    }
}