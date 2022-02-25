package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that interacts with remote currency rates api (binance.com).
 */
@Service
public class BinanceApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger(BinanceApiClient.class);

    /**
     * Get latest currency rates
     *
     * @return {@link Map} <{@link String}, {@link Double}> - latest currency rates.
     * @throws ResponseStatusException in case of too frequent requests.
     */
    public Map<String, Double> getAllCurrencyPrices() throws ResponseStatusException {
        ResponseEntity<List<CurrencyRatesDTO>> responseEntity = null;

        //trying to get new data
        try {
            responseEntity = restTemplate.exchange("https://api.binance.com/api/v3/ticker/price", HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            logger.trace("Got response from Binance api. Status code is: {}", responseEntity.getStatusCode());
        } catch (RestClientException e) {
            logger.error("Exception while fetching new data from binance.com: {}", e.getMessage());
        }

        //handle null responses
        if (responseEntity == null || responseEntity.getBody() == null) {
            logger.error("Got null response from Binance.com");
            return Collections.emptyMap();
        }

        if (responseEntity.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            logger.error("Too much requests to binance.com");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        } else if (responseEntity.getStatusCode() == HttpStatus.I_AM_A_TEAPOT) {
            logger.error("Banned in binance.com api.");
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
        } else {
            //hope that other statuses are okay
            return filterRates(responseEntity.getBody());
        }
    }

    /**
     * This method filters all data that got from external api accordingly to allowed currency rates list hardcoded here.
     *
     * @param unfilteredData {@link List} of {@link CurrencyRatesDTO}: all currencies rates got from external api.
     * @return {@link Map} of filtered data: key - {@link String}: rate name (for example 'BTCRUB'),
     * value - {@link Double}: currencies rate value
     */
    public Map<String, Double> filterRates(List<CurrencyRatesDTO> unfilteredData) {
        Map<String, Double> unfilteredMap = unfilteredData.stream()
                .collect(Collectors.toMap(
                        CurrencyRatesDTO::getSymbol,
                        (CurrencyRatesDTO item) -> Double.parseDouble(item.getPrice())));
        Map<String, Double> filteredData = new HashMap<>();

        List<String> allowedRates = List.of(
                "ETHBTC", "BTCUSDT", "BNBBTC", "XRPBTC", "ADABTC", "BTCRUB", "BTCEUR",
                "ETHUSDT", "BNBETH", "XRPETH", "ADAETH", "ETHRUB", "ETHEUR",
                "BNBUSDT", "XRPUSDT", "ADAUSDT", "USDTRUB", "EURUSDT",
                "XRPBNB", "ADABNB", "BNBRUB", "BNBEUR",
                "XRPRUB", "XRPEUR",
                "ADARUB", "ADAEUR"
        );

        for (String rate : allowedRates) {
            if (unfilteredMap.get(rate) == null) {
                logger.error("not found rate in list of allowed rates!");
            } else {
                filteredData.put(rate, unfilteredMap.get(rate));
            }
        }
        return filteredData;
    }
}