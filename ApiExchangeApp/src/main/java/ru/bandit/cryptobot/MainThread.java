package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.bandit.cryptobot.DAO.Avg1MinuteRatesDAO;
import ru.bandit.cryptobot.DAO.RatesDAO;
import ru.bandit.cryptobot.DAO.TriggersDAO;
import ru.bandit.cryptobot.DTO.TriggerDTO;
import ru.bandit.cryptobot.clients.BinanceApiClient;
import ru.bandit.cryptobot.clients.BotAppClient;
import ru.bandit.cryptobot.service.AverageCountService;
import ru.bandit.cryptobot.triggers.TriggerCompare;

import java.util.List;

@Component
public class MainThread {
    Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    BotAppClient botAppClient;

    @Autowired
    AverageCountService averageCountService;

    @Autowired
    TriggersDAO triggersDAO;

    @Autowired
    RatesDAO ratesDAO;

    @Autowired
    Avg1MinuteRatesDAO averageRates;

    @Autowired
    private TriggerCompare triggerCompare;

    @Autowired
    private BinanceApiClient binanceApiClient;

    @Scheduled(fixedDelay = 5000)
    public void performDataCycle() throws InterruptedException {

        //fetch new data from remote api
        try {
            ratesDAO.setCurrencyRates(binanceApiClient.getAllCurrencyPrices());
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

        //update triggers
        triggersDAO.setTriggersList(botAppClient.getAllTriggers());

        //check triggers
        triggerCompare.checkTriggers(ratesDAO.getCurrencyRates());

        //calculating average
        averageRates.setAverageCurrencyRates(averageCountService.get1MinuteAverage(ratesDAO.getCurrencyRates()));

        //send new rates
        botAppClient.postNewRates(ratesDAO.getCurrencyRates());
        botAppClient.postAverageRates(averageRates.getAverageCurrencyRates());
    }

    @Scheduled(fixedDelay = 15000)
    private void generateRandomTrigger() {
        //fixme this is mock worked trigger generator
        logger.trace("generating trigger");
        List<TriggerDTO> triggers = triggersDAO.getTriggersList();
        if (triggers == null || triggers.isEmpty()) return;
        botAppClient.postWorkedTrigger(triggers.remove(0).getId(), 36.6);
    }
}
