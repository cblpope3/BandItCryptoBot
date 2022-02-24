package ru.bandit.cryptobot.triggers;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.DTO.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TriggerCompare {

    private static final List<Trigger> triggers = new ArrayList<>();

    public void checkTriggers(Map<String, Double> newData) {
        //TODO implement this

    }

    public void addTrigger(Trigger newTrigger) {
        triggers.add(newTrigger);
    }

    public void deleteTrigger(Trigger deleteTrigger) {
        //TODO test how it works
        triggers.remove(deleteTrigger);
    }

    public void replaceAllTriggers(List<Trigger> triggerList) {
        triggers.clear();
        triggers.addAll(triggerList);
    }
}