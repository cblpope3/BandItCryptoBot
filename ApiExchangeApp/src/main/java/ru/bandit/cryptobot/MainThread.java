package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.clients.BinanceApiClient;
import ru.bandit.cryptobot.service.AverageRatesService;
import ru.bandit.cryptobot.service.CurrentRatesService;
import ru.bandit.cryptobot.service.TriggersService;

@Component
public class MainThread {

    Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    CurrentRatesService currentRatesService;

    @Autowired
    AverageRatesService averageRatesService;

    @Autowired
    TriggersService triggersService;

    //todo replace client with service
    @Autowired
    BinanceApiClient binanceApiClient;

    /**
     * Method perform periodic cycle to update currency rates.
     *
     * @throws InterruptedException if cool down timer is interrupted.
     */
    @Scheduled(fixedDelay = 5000)
    public void performDataCycle() throws InterruptedException {

        //fetch new data from remote api
        try {
            currentRatesService.setCurrencyRates(binanceApiClient.getAllCurrencyPrices());
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
