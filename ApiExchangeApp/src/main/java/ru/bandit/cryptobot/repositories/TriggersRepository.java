package ru.bandit.cryptobot.repositories;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class TriggersRepository {
    List<UserTriggerEntity> triggersList = new ArrayList<>();

    public List<UserTriggerEntity> getTriggersList() {
        return triggersList;
    }

    public void setTriggersList(List<UserTriggerEntity> triggersList) {
        this.triggersList = triggersList;
    }

    public void addTrigger(UserTriggerEntity trigger) {
        this.triggersList.add(trigger);
    }

    public void deleteTrigger(Long triggerId) {

        for (UserTriggerEntity trigger : this.triggersList) {
            if (trigger.getId().equals(triggerId)) {
                this.triggersList.remove(trigger);
            }
        }
    }
}
