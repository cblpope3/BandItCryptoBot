package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.clients.BotAppTriggersClient;
import ru.bandit.cryptobot.dao.RatesDAO;
import ru.bandit.cryptobot.dao.TriggersDAO;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class works with triggers (adds, deletes, refreshes and compares to current rates).
 */
@Component
public class TriggersService {

    private final TriggersDAO triggersDAO;
    private final BotAppTriggersClient botAppTriggersClient;
    private final RatesDAO ratesDAO;
    private final Logger logger = LoggerFactory.getLogger(TriggersService.class);

    @Autowired
    public TriggersService(TriggersDAO triggersDAO, BotAppTriggersClient botAppTriggersClient, RatesDAO ratesDAO) {
        this.triggersDAO = triggersDAO;
        this.botAppTriggersClient = botAppTriggersClient;
        this.ratesDAO = ratesDAO;
    }

    /**
     * Method checks which of active triggers are worked.
     *
     * @return {@link Map} of worked trigger id ({@link Long}) -> currency rate ({@link Double})
     */
    public Map<Long, Double> checkTriggers() {

        logger.trace("Checking active triggers:");

        List<TriggerDTO> triggers = triggersDAO.getTriggersList();
        Map<String, Double> currencyRates = ratesDAO.getCurrencyRates();

        if (triggers == null || triggers.isEmpty()) {
            logger.trace("Active triggers list is empty.");
            return Collections.emptyMap();
        }

        //making list of worked triggers
        Map<Long, Double> workedTriggers = new HashMap<>();

        for (TriggerDTO trigger : triggers) {
            Double rateValue = currencyRates.get(trigger.getCurrencyPair());

            if (this.isTriggerWorked(trigger, rateValue)) {

                logger.trace("Got worked trigger: {}! Currency rate for {} is {}.", trigger, trigger.getCurrencyPair(), rateValue);
                workedTriggers.put(trigger.getId(), rateValue);
            }
        }

        //delete worked triggers from triggers dao
        for (Map.Entry<Long, Double> workedTrigger : workedTriggers.entrySet()) {
            triggersDAO.deleteTrigger(workedTrigger.getKey());
        }

        return workedTriggers;
    }

    /**
     * Send {@link Map} of worked triggers to Bot-App
     *
     * @param triggersCollection {@link Map} of trigger id ({@link Long}) -> currency rate ({@link Double})
     */
    public void postWorkedTriggersCollection(Map<Long, Double> triggersCollection) {

        if (triggersCollection == null || triggersCollection.isEmpty()) {
            logger.trace("Abort posting worked triggers to Bot-App: triggers map is empty.");
            return;
        }

        logger.trace("Posting worked triggers collection: {}", triggersCollection);

        for (Map.Entry<Long, Double> workedTrigger : triggersCollection.entrySet()) {
            botAppTriggersClient.postWorkedTrigger(workedTrigger.getKey(), workedTrigger.getValue());
        }
    }

//    /**
//     * Method randomly generates worked trigger. Used for test purposes.
//     *
//     * @param newData {@link Map} of new currency rates.
//     */
//    public void checkTriggersRandom(Map<String, Double> newData) {
//        logger.warn("Generating random trigger.");
//
//        List<TriggerDTO> triggers = triggersDAO.getTriggersList();
//
//        if (triggers == null || triggers.isEmpty()) return;
//
//        int randomTriggerId = (int) (Math.random() * triggers.size());
//
//        this.postWorkedTriggersCollection(Map.of(triggers.remove(randomTriggerId).getId(), 36.6));
//    }

    /**
     * Method adds new trigger to {@link TriggersDAO}.
     *
     * @param newTrigger new trigger to be saved.
     */
    public void addTrigger(TriggerDTO newTrigger) {
        logger.trace("Trying to save new trigger: {}.", newTrigger);
        triggersDAO.addTrigger(newTrigger);
    }

    /**
     * Method deletes trigger with given id from {@link TriggersDAO}
     *
     * @param triggerId id of trigger to be deleted
     */
    public boolean deleteTrigger(Long triggerId) {
        logger.trace("Got command to delete trigger with id={}", triggerId);
        return triggersDAO.deleteTrigger(triggerId);
    }

    /**
     * Method updates all triggers in {@link TriggersDAO}
     */
    public void updateTriggerList() {
        logger.trace("Got update trigger list command.");
        triggersDAO.setTriggersList(botAppTriggersClient.getAllTriggers());
    }

    private boolean isTriggerWorked(TriggerDTO trigger, Double rateValue) {

        return (trigger.getTriggerType().equals(TriggerDTO.TriggerType.UP)
                && rateValue > trigger.getTargetValue())
                ||
                (trigger.getTriggerType().equals(TriggerDTO.TriggerType.DOWN)
                        && rateValue < trigger.getTargetValue());

    }
}