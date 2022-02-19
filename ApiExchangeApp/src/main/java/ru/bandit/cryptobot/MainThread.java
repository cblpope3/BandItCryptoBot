package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.service.BotAppService;
import ru.bandit.cryptobot.triggers.TriggerCompare;

import java.util.HashMap;
import java.util.Map;

@Component
public class MainThread {
    Logger logger = LoggerFactory.getLogger(MainThread.class);

    @Autowired
    private ApiManager apiManager;

    @Autowired
    private TriggerCompare triggerCompare;

    @Autowired
    private BotAppService botAppService;

    //TODO watch hashmap initial settings
    private static Map<String, Double> currencyRates = new HashMap<>();

    //TODO make this method static
    public void performDataCycle() {

        //save new data
        currencyRates = apiManager.getCurrentApi().getAllCurrencyPrices();
        logger.debug("Got new data from api.");
        if (logger.isTraceEnabled()) logger.trace("New data is: {}", currencyRates.toString());

        //repeat to reserve api if needed
        if (!this.isDataCorrect(currencyRates)) {
            //TODO implement
            throw new UnsupportedOperationException();
        }

        //check triggers
        triggerCompare.checkTriggers(currencyRates);

        //send new rates
        botAppService.publishNewRates(currencyRates);

    }

    private boolean isDataCorrect(Map<String, Double> data) {
        //TODO implement
        return true;
    }
}
