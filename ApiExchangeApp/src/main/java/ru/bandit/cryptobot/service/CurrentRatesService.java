package ru.bandit.cryptobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.clients.BotAppCurrentRatesClient;
import ru.bandit.cryptobot.dao.RatesDAO;

import java.util.Map;

/**
 * This class contains logic to interact with {@link BotAppCurrentRatesClient}.
 */
@Service
public class CurrentRatesService extends RatesService {

    private final RatesDAO ratesDAO;

    @Autowired
    public CurrentRatesService(BotAppCurrentRatesClient botAppCurrentRatesClient, RatesDAO ratesDAO) {
        this.ratesDAO = ratesDAO;
        this.botAppRatesClient = botAppCurrentRatesClient;
    }

    /**
     * Get all rates stored in {@link RatesDAO}
     *
     * @return {@link Map} <{@link String}, {@link Double}> of currency rates
     */
    public Map<String, Double> getCurrencyRates() {
        return ratesDAO.getCurrencyRates();
    }

    /**
     * Replace all stored in {@link RatesDAO} currency rates by new {@link Map} of currency rates
     *
     * @param currencyRates {@link Map} <{@link String}, {@link Double}> of new currency rates to save.
     */
    public void setCurrencyRates(Map<String, Double> currencyRates) {
        ratesDAO.setCurrencyRates(currencyRates);
    }
}
