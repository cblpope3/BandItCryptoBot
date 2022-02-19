package ru.bandit.cryptobot.cotroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bandit.cryptobot.entities.RatesJSONContainer;
import ru.bandit.cryptobot.services.RatesService;

@RestController
@RequestMapping("/rates")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    RatesService ratesService;

    @PostMapping("")
    public ResponseEntity<Object> gotNewRates(@RequestBody RatesJSONContainer newRates) {

        if (logger.isTraceEnabled()) logger.trace("Received new rates from api app: {}", newRates.toString());

        ratesService.sendNewRatesToSubscribers(newRates);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}