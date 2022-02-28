package ru.bandit.cryptobot.test_data;

import ru.bandit.cryptobot.dto.CurrencyRatesDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;

public abstract class CommonData {
    //Currency rates
    public static String testRateSymbol1 = "BTCRUB";
    public static String testRateSymbol2 = "ETHRUB";

    public static String testRateSymbolNotAllowed1 = "Rate1";
    public static String testRateSymbolNotAllowed2 = "Rate2";

    public static Double testRateValue1_1 = 100.1;
    public static Double testRateValue2_1 = 200.2;
    public static Double testRateValue1_2 = 300.3;
    public static Double testRateValue2_2 = 400.4;

    public final static CurrencyRatesDTO currencyRate1 = new CurrencyRatesDTO(testRateSymbol1, testRateValue1_1.toString());
    public final static CurrencyRatesDTO currencyRate2 = new CurrencyRatesDTO(testRateSymbol2, testRateValue2_1.toString());
    public final static CurrencyRatesDTO currencyRateNotAllowed1 = new CurrencyRatesDTO(testRateSymbolNotAllowed1, testRateValue1_1.toString());
    public final static CurrencyRatesDTO currencyRateNotAllowed2 = new CurrencyRatesDTO(testRateSymbolNotAllowed2, testRateValue1_1.toString());

    //Triggers
    public final static Long trigger1Id = 234L;
    public final static Long trigger2Id = 432L;
    public final static Long triggerIdWrong = 11L;

    public final static Double trigger1Value = 23.4;
    public final static Double trigger2Value = 43.2;

    public final static TriggerDTO testTrigger1 = new TriggerDTO(trigger1Id, currencyRate1.getSymbol(),
            trigger1Value, TriggerDTO.TriggerType.UP);
    public final static TriggerDTO testTrigger2 = new TriggerDTO(trigger2Id, currencyRate2.getSymbol(),
            trigger2Value, TriggerDTO.TriggerType.DOWN);
}
