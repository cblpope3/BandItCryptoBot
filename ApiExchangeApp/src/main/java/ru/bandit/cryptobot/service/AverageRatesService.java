package ru.bandit.cryptobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.clients.BotAppAverageRatesClient;
import ru.bandit.cryptobot.dao.RatesArchiveDAO;
import ru.bandit.cryptobot.dao.RatesDAO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class performs calculation average values of currency rates.
 */
@Service
public class AverageRatesService extends RatesService {

    private final RatesArchiveDAO ratesArchiveDAO;
    private final RatesDAO ratesDAO;

    @Autowired
    public AverageRatesService(BotAppAverageRatesClient botAppAverageRatesClient, RatesDAO ratesDAO, RatesArchiveDAO ratesArchiveDAO) {
        this.ratesArchiveDAO = ratesArchiveDAO;
        this.ratesDAO = ratesDAO;
        this.botAppRatesClient = botAppAverageRatesClient;
    }

    /**
     * Method calculates new 1-minute moving average values of all currency rates stored in archive.
     */
    public Map<String, Double> calculateNew1MinuteAverages() {

        ratesArchiveDAO.appendNewRatesToArchive(ratesDAO.getCurrencyRates());

        List<Map<String, Double>> storedCurrencyRates = ratesArchiveDAO.getCurrencyRatesArchive();

        return storedCurrencyRates.stream()
                .flatMap(a -> a.entrySet().stream())
                .collect(
                        Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingDouble(Map.Entry::getValue))
                );
    }
}
