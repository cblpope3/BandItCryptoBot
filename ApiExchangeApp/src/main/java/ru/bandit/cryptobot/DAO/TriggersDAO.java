package ru.bandit.cryptobot.DAO;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.DTO.triggers.UserTriggerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all available triggers inside it. Implements singleton design pattern.
 */
@Component
public class TriggersDAO {
    private final List<UserTriggerEntity> triggersList = new ArrayList<>();

    /**
     * Get all triggers stored in this class
     *
     * @return {@link List} of {@link UserTriggerEntity}
     */
    public List<UserTriggerEntity> getTriggersList() {
        return triggersList;
    }

    /**
     * Replace all stored triggers by new {@link List} of {@link UserTriggerEntity}
     *
     * @param triggersList {@link List} of new {@link UserTriggerEntity} to save.
     */
    public void setTriggersList(List<UserTriggerEntity> triggersList) {
        this.triggersList.clear();
        this.triggersList.addAll(triggersList);
    }

    /**
     * Add new trigger to trigger storage
     *
     * @param trigger new {@link UserTriggerEntity} to save in storage.
     */
    public void addTrigger(UserTriggerEntity trigger) {
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
