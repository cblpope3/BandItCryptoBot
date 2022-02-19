package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.data_containers.BinanceResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Qualifier("Binance")
public class BinanceApiService implements ApiService {

    Logger logger = LoggerFactory.getLogger(BinanceApiService.class);

    private RestTemplate restTemplate = new RestTemplate();

    public Map<String, Double> getAllCurrencyPrices() throws ResponseStatusException {
        ResponseEntity<List<BinanceResponse>> responseEntity =
                restTemplate.exchange("https://api.binance.com/api/v3/ticker/price", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        HttpStatus statusCode = responseEntity.getStatusCode();
        logger.debug("Got response from Binance api. Status code is: {}", statusCode);
        if (statusCode.value() == 200) {
            //TODO must be tested to null response
            return Objects.requireNonNull(responseEntity.getBody()).stream()
                    .collect(Collectors.toMap(
                            BinanceResponse::getSymbol,
                            (BinanceResponse item) -> Double.parseDouble(item.getPrice())));
        } else {
            logger.error("Api response status code is not 200! Code: {}", statusCode);
            //not sure that processing error code through exception is good idea
            throw new ResponseStatusException(statusCode);
        }
    }
}