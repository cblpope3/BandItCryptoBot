package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.service.AverageRatesService;
import ru.bandit.cryptobot.service.BinanceApiService;
import ru.bandit.cryptobot.service.CurrentRatesService;
import ru.bandit.cryptobot.service.TriggersService;

@Component
public class MainThread {

    private final Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    CurrentRatesService currentRatesService;

    @Autowired
    AverageRatesService averageRatesService;

    @Autowired
    TriggersService triggersService;

    @Autowired
    BinanceApiService binanceApiService;

    /**
     * Method perform periodic cycle to update currency rates.
     */
    @Scheduled(fixedDelay = 5000)
    public void performDataCycle() {

        //fetch new data from remote api
        try {
            binanceApiService.getNewCurrencyRates();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        //check triggers
        triggersService.postWorkedTriggersCollection(triggersService.checkTriggers());

        //send new rates
        currentRatesService.publishNewRates(currentRatesService.getCurrencyRates());
        averageRatesService.publishNewRates(averageRatesService.calculateNew1MinuteAverages());
    }

    /**
     * Method perform periodic active triggers list synchronisation with Bot-App.
     */
    @Scheduled(fixedDelay = 20000)
    public void periodicTriggersSync() {
        //update triggers
        triggersService.updateTriggerList();
    }
}
