package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.data_containers.BinanceResponse;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BotAppService {

    private final RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger(BotAppService.class);

    @Value("${bot-app.hostname}")
    private String botAppCurrencyUrl;

    public void publishNewRates(Map<String, Double> newRates) {

        String response = "";
        try {
            response = restTemplate.postForObject(botAppCurrencyUrl + "rates", convertData(newRates), String.class);
        } catch (RestClientException e) {
            logger.warn("Trying to send new rates to bot-app. Client not responding: {}", e.getMessage());
        }

        //TODO use resttemplate.exchange to specify response status
        logger.trace("Rates have been sent to Bot App. Response is: {}", response);
    }

    public void publishAverageRates(Map<String, Double> averageRates) {
        String response = "";
        try {
            response = restTemplate.postForObject(botAppCurrencyUrl + "rates/1m_avg", convertData(averageRates), String.class);
        } catch (RestClientException e) {
            logger.warn("Trying to send average values to bot-app. Client not responding: {}", e.getMessage());
        }
        logger.trace("Average values have been sent to Bot App. Response is: {}", response);
    }

    public List<UserTriggerEntity> requestAllTriggers() {

        ResponseEntity<List<UserTriggerEntity>> responseEntity;

        responseEntity = restTemplate.exchange(botAppCurrencyUrl + "trigger/getAllTarget", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        logger.trace("Triggers list requested from Bot App. Response status is: {}", responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    public void sendWorkedTrigger(Long triggerId, Double value) {

        String requestUrl = String.format("%strigger/worked?triggerId=%d&value=%f", botAppCurrencyUrl, triggerId, value);

        restTemplate.postForObject(requestUrl, null, String.class);

        logger.trace("Worked trigger #{} sent to Bot App.", triggerId);

    }

    private List<BinanceResponse> convertData(Map<String, Double> input) {
        List<BinanceResponse> result = new ArrayList<>();

        for (Map.Entry<String, Double> rate : input.entrySet()) {
            result.add(new BinanceResponse(rate.getKey(), rate.getValue().toString()));
        }

        return result;
    }
}