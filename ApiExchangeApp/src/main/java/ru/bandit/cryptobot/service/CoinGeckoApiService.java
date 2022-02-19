package ru.bandit.cryptobot.service;

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
@Qualifier("CoinGecko")
public class CoinGeckoApiService implements ApiService{


    //TODO copied from BinanceApiService
    private static final String SERVICE_URI = "https://api.binance.com/api/v3/ticker/price";
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Double> getAllCurrencyPrices() throws ResponseStatusException {
        ResponseEntity<List<BinanceResponse>> responseEntity =
                restTemplate.exchange(SERVICE_URI, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<BinanceResponse>>() {
                        }
                );
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.value() == 200) {
            //TODO must be tested to null response
            return Objects.requireNonNull(responseEntity.getBody()).stream()
                    .collect(Collectors.toMap(
                            BinanceResponse::getSymbol,
                            (BinanceResponse item) -> Double.parseDouble(item.getPrice())));
        } else {
            //not sure that processing error code through exception is good idea
            throw new ResponseStatusException(statusCode);
        }
    }
}