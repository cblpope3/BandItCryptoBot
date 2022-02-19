package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BotAppService {

    private final RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger(BotAppService.class);

    @Value("${bot-app.hostname}")
    private String botAppCurrencyUrl;

    public void publishNewRates(Map<String, Double> newRates) {

        String response;

        response = restTemplate.postForObject(botAppCurrencyUrl + "rates", newRates, String.class);

        logger.debug("Rates have been sent to Bot App. Response is: {}", response);
    }

    public void requestAllTriggers() {
        //TODO implement
        throw new UnsupportedOperationException();
    }

    public void sendWorkedTrigger() {
        //TODO implement
        throw new UnsupportedOperationException();
    }
}