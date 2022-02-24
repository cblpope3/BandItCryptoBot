package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;
import ru.bandit.cryptobot.DAO.TriggersDAO;
import ru.bandit.cryptobot.service.AverageCountService;
import ru.bandit.cryptobot.service.BinanceApiService;
import ru.bandit.cryptobot.clients.BotAppClient;
import ru.bandit.cryptobot.triggers.TriggerCompare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MainThread {
    Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    BotAppClient botAppClient;
    @Autowired
    private TriggerCompare triggerCompare;
    @Autowired
    private BinanceApiService binanceApiService;
    @Autowired
    AverageCountService averageCountService;

    @Autowired
    TriggersDAO triggersDAO;

    private Map<String, Double> currencyRates = new HashMap<>();
    private Map<String, Double> average1MinuteRates = new HashMap<>();

    @Scheduled(fixedDelay = 5000)
    public void performDataCycle() {

        //save new data
        currencyRates = binanceApiService.getAllCurrencyPrices();
        logger.debug("Got new data from api.");

        //update triggers
        triggersDAO.setTriggersList(botAppClient.getAllTriggers());

        //check triggers
        triggerCompare.checkTriggers(currencyRates);

        //calculating average
        average1MinuteRates = averageCountService.get1MinuteAverage(currencyRates);

        //send new rates
        botAppClient.postNewRates(currencyRates);
        botAppClient.postAverageRates(average1MinuteRates);
    }

    @Scheduled(fixedDelay = 15000)
    private void generateRandomTrigger() {
        logger.trace("generating trigger");
        List<UserTriggerEntity> triggers = triggersDAO.getTriggersList();
        if (triggers == null || triggers.isEmpty()) return;
        botAppClient.postWorkedTrigger(triggers.remove(0).getId(), 36.6);
    }
}
