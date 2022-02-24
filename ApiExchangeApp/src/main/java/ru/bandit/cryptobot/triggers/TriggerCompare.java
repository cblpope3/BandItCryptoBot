package ru.bandit.cryptobot.triggers;

import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.DTO.TriggerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TriggerCompare {

    private static final List<TriggerDTO> triggers = new ArrayList<>();

    public void checkTriggers(Map<String, Double> newData) {
        //TODO implement this

    }

    public void addTrigger(TriggerDTO newTrigger) {
        triggers.add(newTrigger);
    }

    public void deleteTrigger(TriggerDTO deleteTrigger) {
        //TODO test how it works
        triggers.remove(deleteTrigger);
    }

    public void replaceAllTriggers(List<TriggerDTO> triggerList) {
        triggers.clear();
        triggers.addAll(triggerList);
    }
}