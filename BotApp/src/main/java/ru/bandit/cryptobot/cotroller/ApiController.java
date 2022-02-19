package ru.bandit.cryptobot.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bandit.cryptobot.Bot;
import ru.bandit.cryptobot.entities.RatesJSONContainer;

@RestController
@RequestMapping("/rates")
public class ApiController {

    @Autowired
    Bot telegramBot;

    @PostMapping("")
    public String gotNewRates(@RequestBody RatesJSONContainer newRates) {
        //TODO this is dummy method
        System.out.println("got new rates: " + newRates.toString());
        telegramBot.sendMessageToMe(newRates.toString());
        return "Got new data: " + newRates;
    }
}