package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bandit.cryptobot.clients.BotAppRatesClient;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RatesService {

    protected Logger logger = LoggerFactory.getLogger(RatesService.class);

    protected BotAppRatesClient botAppRatesClient;

    /**
     * Converts data from {@link Map} <{@link String}, {@link Double}> format to {@link Set}<{@link CurrencyRatesDTO}>.
     *
     * @param input convertable {@link Map} of currency rates.
     * @return {@link Set} of {@link CurrencyRatesDTO}.
     */
    protected Set<CurrencyRatesDTO> convertCurrencyRatesToList(Map<String, Double> input) {
        Set<CurrencyRatesDTO> result = new HashSet<>();

        for (Map.Entry<String, Double> rate : input.entrySet()) {
            result.add(new CurrencyRatesDTO(rate.getKey(), rate.getValue().toString()));
        }

        return result;
    }

    /**
     * Publish new currency rates to Bot-App.
     *
     * @param newRates {@link Map} of new rates symbol({@link String})->value({@link Double}).
     */
    public void publishNewRates(Map<String, Double> newRates) {
        if (newRates == null || newRates.isEmpty()) {
            logger.error("Trying to publish empty rates.");
            return;
        }
        botAppRatesClient.postNewRates(convertCurrencyRatesToList(newRates));
    }
}
