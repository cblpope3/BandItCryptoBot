package ru.bandit.cryptobot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.clients.BotAppClient;
import ru.bandit.cryptobot.dao.TriggersDAO;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.List;
import java.util.Map;

/**
 * This class works with triggers (adds, deletes, refreshes and compares to current rates).
 */
@Component
public class TriggersService {

    Logger logger = LoggerFactory.getLogger(TriggersService.class);


    @Autowired
    TriggersDAO activeTriggers;

    @Autowired
    BotAppClient botAppClient;

    /**
     * Method check which of active triggers are worked.
     *
     * @param newData {@link Map} of new currency rates.
     */
    public void checkTriggers(Map<String, Double> newData) {
        //TODO this is mock. need to implement this correctly
        logger.error("Generating random trigger.");

        List<TriggerDTO> triggers = activeTriggers.getTriggersList();

        if (triggers == null || triggers.isEmpty()) return;

        int randomTriggerId = (int) (Math.random() * triggers.size());

        postWorkedTriggersToBotApp(Map.of(triggers.remove(randomTriggerId).getId(), 36.6));
    }

    /**
     * Method sends worked triggers to Bot-App via api client and then removes it from {@link TriggersDAO}.
     *
     * @param workedTriggersMap {@link Map} of worked triggers ids and currency values.
     */
    public void postWorkedTriggersToBotApp(Map<Long, Double> workedTriggersMap) {
        for (Map.Entry<Long, Double> workedTrigger : workedTriggersMap.entrySet()) {
            botAppClient.postWorkedTrigger(workedTrigger.getKey(), workedTrigger.getValue());
            deleteTrigger(workedTrigger.getKey());
        }

    }

    /**
     * Method adds new trigger to {@link TriggersDAO}.
     *
     * @param newTrigger new trigger to be saved.
     */
    public void addTrigger(TriggerDTO newTrigger) {
        activeTriggers.addTrigger(newTrigger);
    }

    /**
     * Method deletes trigger with given id from {@link TriggersDAO}
     *
     * @param triggerId id of trigger to be deleted
     */
    public void deleteTrigger(Long triggerId) {
        activeTriggers.deleteTrigger(triggerId);
    }

    /**
     * Method updates all triggers in {@link TriggersDAO}
     */
    public void updateTriggerList() {
        activeTriggers.setTriggersList(botAppClient.getAllTriggers());
    }
}