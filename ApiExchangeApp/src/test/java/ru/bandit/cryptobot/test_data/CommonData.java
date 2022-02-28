package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;

import java.util.List;
import java.util.Map;

public abstract class CommonData {
    //Triggers
    public final static Long trigger1Id = 100L;
    public final static Long trigger2Id = 200L;
    public final static Long triggerWorked1Id = 300L;
    public final static Long triggerWorked2Id = 400L;
    public final static Long triggerIdWrong = 11L;
    public final static Double trigger1Value = 234.4;
    public final static Double trigger2Value = 132.2;
    public final static Double triggerWorked1Value = 20.4;
    public final static Double triggerWorked2Value = 300.2;
    //Currency rates
    public static String testRateSymbol1 = "BTCRUB";
    public static String testRateSymbol2 = "ETHRUB";
    public static String testRateSymbolNotAllowed1 = "Rate1";
    public static String testRateSymbolNotAllowed2 = "Rate2";
    public static Double testRateValue1_1 = 100.1;
    public final static CurrencyRatesDTO currencyRate1 = new CurrencyRatesDTO(testRateSymbol1, testRateValue1_1.toString());
    public final static TriggerDTO testTrigger1 = new TriggerDTO(trigger1Id, currencyRate1.getSymbol(),
            trigger1Value, TriggerDTO.TriggerType.UP);
    public final static TriggerDTO testTriggerWorked1 = new TriggerDTO(triggerWorked1Id, currencyRate1.getSymbol(),
            triggerWorked1Value, TriggerDTO.TriggerType.UP);
    public final static CurrencyRatesDTO currencyRateNotAllowed1 = new CurrencyRatesDTO(testRateSymbolNotAllowed1, testRateValue1_1.toString());
    public final static CurrencyRatesDTO currencyRateNotAllowed2 = new CurrencyRatesDTO(testRateSymbolNotAllowed2, testRateValue1_1.toString());
    public static Double testRateValue2_1 = 200.2;
    public final static CurrencyRatesDTO currencyRate2 = new CurrencyRatesDTO(testRateSymbol2, testRateValue2_1.toString());
    public final static TriggerDTO testTrigger2 = new TriggerDTO(trigger2Id, currencyRate2.getSymbol(),
            trigger2Value, TriggerDTO.TriggerType.DOWN);
    public final static TriggerDTO testTriggerWorked2 = new TriggerDTO(triggerWorked2Id, currencyRate2.getSymbol(),
            triggerWorked2Value, TriggerDTO.TriggerType.DOWN);
    public final static List<TriggerDTO> testTriggersList = List.of(testTrigger1, testTriggerWorked1, testTrigger2, testTriggerWorked2);
    public final static Map<Long, Double> workedTriggersMap = Map.of(triggerWorked1Id, testRateValue1_1, triggerWorked2Id, testRateValue2_1);
    public static Double testRateValue1_2 = 300.3;
    public static Double testRateValue2_2 = 400.4;
}
