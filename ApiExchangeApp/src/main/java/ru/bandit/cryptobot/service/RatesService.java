package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.clients.BotAppRatesClient;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains logic to interact with {@link BotAppRatesClient}.
 */
@Service
public class RatesService {

    private final BotAppRatesClient botAppRatesClient;
    Logger logger = LoggerFactory.getLogger(RatesService.class);

    @Autowired
    public RatesService(BotAppRatesClient botAppRatesClient) {
        this.botAppRatesClient = botAppRatesClient;
    }

    /**
     * Publish new currencies rates values to Bot-App
     *
     * @param newRates {@link Map} of new rates symbol({@link String})->value({@link Double})
     */
    public void publishNewRates(Map<String, Double> newRates) {
        if (newRates == null || newRates.isEmpty()) {
            logger.error("Trying to publish empty rates.");
            return;
        }
        botAppRatesClient.postNewRates(convertCurrencyRatesToList(newRates));
    }

    /**
     * Publish new average currencies rates values to Bot-App
     *
     * @param newRates {@link Map} of new rates symbol({@link String})->value({@link Double})
     */
    public void publishNew1MinuteAverageRates(Map<String, Double> newRates) {
        if (newRates == null || newRates.isEmpty()) {
            logger.error("Trying to publish empty average rates.");
            return;
        }
        botAppRatesClient.postAverageRates(convertCurrencyRatesToList(newRates));
    }

    /**
     * Converts data from {@link Map} <{@link String}, {@link Double}> format to {@link List}<{@link CurrencyRatesDTO}>.
     *
     * @param input convertable {@link Map} of currency rates
     * @return {@link List} of {@link CurrencyRatesDTO}
     */
    private List<CurrencyRatesDTO> convertCurrencyRatesToList(Map<String, Double> input) {
        List<CurrencyRatesDTO> result = new ArrayList<>();

        for (Map.Entry<String, Double> rate : input.entrySet()) {
            result.add(new CurrencyRatesDTO(rate.getKey(), rate.getValue().toString()));
        }

        return result;
    }
}
