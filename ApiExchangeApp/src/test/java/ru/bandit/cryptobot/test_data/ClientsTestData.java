package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientsTestData {

    public final static Long trigger1Id = 234L;
    public final static Long trigger2Id = 432L;
    public final static Long triggerIdWrong = 11L;

    public final static Double trigger1Value = 23.4;
    public final static Double trigger2Value = 43.2;
    private final static CurrencyRatesDTO currencyRates1 = new CurrencyRatesDTO("BTCRUB", "100.1");
    public final static TriggerDTO testTrigger1 = new TriggerDTO(trigger1Id, currencyRates1.getSymbol(),
            trigger1Value, TriggerDTO.TriggerType.UP);
    private final static CurrencyRatesDTO currencyRates2 = new CurrencyRatesDTO("ETHRUB", "200.2");
    public final static TriggerDTO testTrigger2 = new TriggerDTO(trigger2Id, currencyRates2.getSymbol(),
            trigger2Value, TriggerDTO.TriggerType.DOWN);

    private final static List<CurrencyRatesDTO> testArrayList = new ArrayList<>(Arrays.asList(currencyRates1, currencyRates2));

    private final static String expectedResponse = String.format("[{\"symbol\":\"%s\",\"price\":\"%s\"},{\"symbol\":\"%s\",\"price\":\"%s\"}]",
            currencyRates1.getSymbol(), currencyRates1.getPrice(),
            currencyRates2.getSymbol(), currencyRates2.getPrice());

    public static String getTestJson() {
        return expectedResponse;
    }

    public static List<CurrencyRatesDTO> getTestArrayList() {
        return testArrayList;
    }

    public static String getTriggerJson() {
        return String.format("{\"triggerId\":%d,\"currencyPair\":\"%s\",\"targetValue\":%s,\"triggerType\":\"%s\"}",
                testTrigger1.getId(), testTrigger1.getCurrencyPair(), testTrigger1.getTargetValue().toString(),
                testTrigger1.getTriggerType().getTitle());
    }

    public static String getWrongTriggerJson1() {
        return String.format("{\"currencyPair\":\"%s\",\"targetValue\":%s,\"triggerType\":\"%s\"}",
                testTrigger1.getCurrencyPair(), testTrigger1.getTargetValue().toString(),
                testTrigger1.getTriggerType().getTitle());
    }

    public static String getWrongTriggerJson2() {
        return String.format("{\"triggerId\":%d,\"targetValue\":%s,\"triggerType\":\"%s\"}",
                testTrigger1.getId(), testTrigger1.getTargetValue().toString(),
                testTrigger1.getTriggerType().getTitle());
    }

    public static String getWrongTriggerJson3() {
        return String.format("{\"triggerId\":%d,\"currencyPair\":\"%s\",\"triggerType\":\"%s\"}",
                testTrigger1.getId(), testTrigger1.getCurrencyPair(),
                testTrigger1.getTriggerType().getTitle());
    }

    public static String getWrongTriggerJson4() {
        return String.format("{\"triggerId\":%d,\"currencyPair\":\"%s\",\"targetValue\":%s}",
                testTrigger1.getId(), testTrigger1.getCurrencyPair(), testTrigger1.getTargetValue().toString());
    }

    public static String getTriggersJson() {
        return String.format("[{\"triggerId\":%d,\"currencyPair\":\"%s\",\"targetValue\":%s,\"triggerType\":\"%s\"}, " +
                        "{\"triggerId\":%d,\"currencyPair\":\"%s\",\"targetValue\":%s,\"triggerType\":\"%s\"}]",
                testTrigger1.getId(), testTrigger1.getCurrencyPair(), testTrigger1.getTargetValue().toString(),
                testTrigger1.getTriggerType().getTitle(),
                testTrigger2.getId(), testTrigger2.getCurrencyPair(), testTrigger2.getTargetValue().toString(),
                testTrigger2.getTriggerType().getTitle());
    }
}