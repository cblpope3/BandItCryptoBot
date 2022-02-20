package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.TriggersRepository;
import ru.bandit.cryptobot.service.AverageCountService;
import ru.bandit.cryptobot.service.BinanceApiService;
import ru.bandit.cryptobot.service.BotAppService;
import ru.bandit.cryptobot.triggers.TriggerCompare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MainThread {
    Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    BotAppService botAppService;
    @Autowired
    private TriggerCompare triggerCompare;
    @Autowired
    private BinanceApiService binanceApiService;
    @Autowired
    AverageCountService averageCountService;

    @Autowired
    TriggersRepository triggersRepository;

    private Map<String, Double> currencyRates = new HashMap<>();
    private Map<String, Double> average1MinuteRates = new HashMap<>();

    @Scheduled(fixedDelay = 5000)
    public void performDataCycle() {

        //save new data
        currencyRates = binanceApiService.getAllCurrencyPrices();
        logger.debug("Got new data from api.");

        //update triggers
        triggersRepository.setTriggersList(botAppService.requestAllTriggers());

        //check triggers
        triggerCompare.checkTriggers(currencyRates);

        //calculating average
        average1MinuteRates = averageCountService.get1MinuteAverage(currencyRates);

        //send new rates
        botAppService.publishNewRates(currencyRates);
        botAppService.publishAverageRates(average1MinuteRates);
    }

    @Scheduled(fixedDelay = 15000)
    private void generateRandomTrigger() {
        logger.trace("generating trigger");
        List<UserTriggerEntity> triggers = triggersRepository.getTriggersList();
        if (triggers == null || triggers.isEmpty()) return;
        botAppService.sendWorkedTrigger(triggers.remove(0).getId(), 36.6);
    }
}
