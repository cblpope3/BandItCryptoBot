package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.TriggerDTO;

public class TriggersTestData extends CommonTestData {
    private final static Long trigger1Id = 111L;
    private final static Long trigger2Id = 222L;

    private final static Double trigger1Value = 100.1;
    private final static Double trigger2Value = 200.2;

    private final static TriggerDTO trigger1 = new TriggerDTO(trigger1Id, currencyPairBTCEUR, trigger1Value, TriggerDTO.TriggerType.TARGET_UP);
    private final static TriggerDTO trigger2 = new TriggerDTO(trigger2Id, currencyPairETHRUB, trigger2Value, TriggerDTO.TriggerType.TARGET_DOWN);

    public static TriggerDTO getTrigger() {
        return trigger1;
    }
}
