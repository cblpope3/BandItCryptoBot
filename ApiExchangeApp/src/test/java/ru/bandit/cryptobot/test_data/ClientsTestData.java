package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.*;

public class ClientsTestData extends CommonData {

    private final static List<CurrencyRatesDTO> testArrayList = new ArrayList<>(Arrays.asList(currencyRate1, currencyRate2));
    private final static Set<CurrencyRatesDTO> testArraySet = new HashSet<>(testArrayList);

    private final static String expectedResponse = String.format("[{\"symbol\":\"%s\",\"price\":\"%s\"},{\"symbol\":\"%s\",\"price\":\"%s\"}]",
            currencyRate1.getSymbol(), currencyRate1.getPrice(),
            currencyRate2.getSymbol(), currencyRate2.getPrice());

    public static String getTestJson() {
        return expectedResponse;
    }

    public static List<CurrencyRatesDTO> getTestArrayList() {
        return testArrayList;
    }

    public static Set<CurrencyRatesDTO> getTestArraySet() {
        return testArraySet;
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