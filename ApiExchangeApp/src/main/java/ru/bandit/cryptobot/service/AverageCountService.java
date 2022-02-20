package ru.bandit.cryptobot.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AverageCountService {
    public Map<String, Double> get1MinuteAverage(Map<String, Double> currentRates) {
        //todo here must be some magic, that calculate average value
        return currentRates;
    }

}
