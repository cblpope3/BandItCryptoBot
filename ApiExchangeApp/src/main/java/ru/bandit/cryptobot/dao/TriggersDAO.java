package ru.bandit.cryptobot.dao;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all available triggers inside it. Implements singleton design pattern.
 */
@Component
public class TriggersDAO {

    private final List<TriggerDTO> triggersList = new ArrayList<>();

    /**
     * Get all triggers stored in this class
     *
     * @return {@link List} of {@link TriggerDTO}
     */
    public List<TriggerDTO> getTriggersList() {
        return triggersList;
    }

    /**
     * Replace all stored triggers by new {@link List} of {@link TriggerDTO}
     *
     * @param triggersList {@link List} of new {@link TriggerDTO} to save.
     */
    public void setTriggersList(List<TriggerDTO> triggersList) {
        this.triggersList.clear();
        this.triggersList.addAll(triggersList);
    }

    /**
     * Add new trigger to trigger storage
     *
     * @param trigger new {@link TriggerDTO} to save in storage.
     */
    public void addTrigger(TriggerDTO trigger) {
        this.triggersList.add(trigger);
    }

    /**
     * Delete trigger with given ID.
     *
     * @param triggerId id of trigger to remove.
     * @return true if trigger was removed, false if trigger with given id not found in storage.
     */
    public boolean deleteTrigger(Long triggerId) {
        return this.triggersList.removeIf(trigger -> trigger.getId().equals(triggerId));
    }
}
