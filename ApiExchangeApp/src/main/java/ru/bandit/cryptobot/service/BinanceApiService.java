package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.clients.BinanceApiClient;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BinanceApiService {

    private final Logger logger = LoggerFactory.getLogger(BinanceApiService.class);

    private final BinanceApiClient binanceApiClient;

    private final RatesDAO ratesDAO;

    //this list limits possible currency rates
    private final List<String> allowedRates = List.of(
            "ETHBTC", "BTCUSDT", "BNBBTC", "XRPBTC", "ADABTC", "BTCRUB", "BTCEUR",
            "ETHUSDT", "BNBETH", "XRPETH", "ADAETH", "ETHRUB", "ETHEUR",
            "BNBUSDT", "XRPUSDT", "ADAUSDT", "USDTRUB", "EURUSDT",
            "XRPBNB", "ADABNB", "BNBRUB", "BNBEUR",
            "XRPRUB", "XRPEUR",
            "ADARUB", "ADAEUR"
    );

    @Autowired
    public BinanceApiService(BinanceApiClient binanceApiClient, RatesDAO ratesDAO) {
        this.binanceApiClient = binanceApiClient;
        this.ratesDAO = ratesDAO;
    }

    /**
     * This method gets new currency rates from {@link BinanceApiClient} and saves them to {@link ru.bandit.cryptobot.dao.RatesDAO}
     *
     * @throws InterruptedException if cool down timer is interrupted.
     */
    public void getNewCurrencyRates() throws InterruptedException {

        List<CurrencyRatesDTO> newCurrencies = new ArrayList<>();

        try {
            newCurrencies.addAll(binanceApiClient.getAllCurrencyPrices());
            logger.debug("Got new data from api.");
        } catch (ResponseStatusException e) {
            if (e.getStatus() == HttpStatus.TOO_MANY_REQUESTS) {
                logger.error("Too frequent requests, need to cool down. Sleeping for 60 seconds.");
                Thread.sleep(60000);
                logger.info("Woke up from sleeping.");
            } else if (e.getStatus() == HttpStatus.I_AM_A_TEAPOT) {
                logger.error("We banned from Binance.com.");
                System.exit(1);
            }
        }

        Map<String, Double> newRates = this.convertRatesListToMap(newCurrencies);
        ratesDAO.setCurrencyRates(this.filterRates(newRates));
    }

    /**
     * This method converts {@link List} of {@link CurrencyRatesDTO} to {@link Map}.
     *
     * @param currencyRatesList input list of currency ratest ({@link List}<{@link CurrencyRatesDTO}>).
     * @return {@link Map} of symbol({@link String})->value({@link Double}).
     */
    private Map<String, Double> convertRatesListToMap(List<CurrencyRatesDTO> currencyRatesList) {
        return currencyRatesList.stream()
                .collect(Collectors.toMap(
                        CurrencyRatesDTO::getSymbol,
                        (CurrencyRatesDTO item) -> Double.parseDouble(item.getPrice())));
    }


    /**
     * This method filters all data that got from external api accordingly to allowed currency rates list hardcoded here.
     *
     * @param unfilteredData {@link List} of {@link CurrencyRatesDTO}: all currencies rates got from external api.
     * @return {@link Map} of filtered data: key - {@link String}: rate name (for example 'BTCRUB'),
     * value - {@link Double}: currencies rate value
     */
    private Map<String, Double> filterRates(Map<String, Double> unfilteredData) {

        Map<String, Double> filteredData = new HashMap<>();

        for (String rate : allowedRates) {
            if (unfilteredData.get(rate) == null) {
                logger.error("Not found allowed rate in new rates list: " + rate);
            } else {
                filteredData.put(rate, unfilteredData.get(rate));
            }
        }
        return filteredData;
    }
}
