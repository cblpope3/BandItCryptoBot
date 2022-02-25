package ru.bandit.cryptobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dao.Avg1MinuteRatesDAO;
import ru.bandit.cryptobot.dao.RatesDAO;

import java.util.Map;

/**
 * This class works with average values of currency rates.
 */
@Service
public class AverageCountService {

    @Autowired
    Avg1MinuteRatesDAO averageDAO;

    @Autowired
    RatesDAO ratesDAO;

    /**
     * Method calculates new 1-minute moving average values of all currency rates.
     */
    public void calculateNew1MinuteAverages() {
        //todo here must be some magic, that calculate average value
        averageDAO.setAverageCurrencyRates(ratesDAO.getCurrencyRates());
    }

    /**
     * Method return current values of 1-minute moving average of all currency rates.
     *
     * @return {@link Map} of currency pair name ({@link String}, for example "BTCEUR");
     * and 1-minute average value ({@link Double}).
     */
    public Map<String, Double> get1MinuteAverages() {
        return averageDAO.getAverageCurrencyRates();
    }

}
