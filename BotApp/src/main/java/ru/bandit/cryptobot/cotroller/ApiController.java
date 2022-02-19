package ru.bandit.cryptobot.cotroller;

import io.swagger.annotations.*;
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

@Api(tags = {"Rates"}, value = "Rates", description = "Cryptocurrency rates API")
@RestController
@RequestMapping("/rates")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    RatesService ratesService;

    @ApiOperation(value = "Отправить последние полученные котировки", nickname = "newRates",
            notes = "Сюда отправляются последние актуальные данные о котировках криптовалют.", tags = {"Rates",})
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted."),
            @ApiResponse(code = 400, message = "Bad request.")})
    @PostMapping("")
    public ResponseEntity<Object> gotNewRates(@ApiParam(value = "Новые котировки криптовалют как JSON объект.")
                                                  @RequestBody RatesJSONContainer newRates) {

        if (logger.isTraceEnabled()) logger.trace("Received new rates from api app: {}", newRates.toString());

        ratesService.sendNewRatesToSubscribers(newRates);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}