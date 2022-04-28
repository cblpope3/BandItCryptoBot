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
import ru.bandit.cryptobot.dao.AverageCurrencyRatesDAO;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class that handle http requests to /rates address.
 */
@SuppressWarnings("unused")
@Api(tags = {"Rates"}, value = "Rates", description = "Cryptocurrency rates API")
@RestController
@RequestMapping("/rates")
public class RatesController {

    Logger logger = LoggerFactory.getLogger(RatesController.class);


    @Autowired
    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    @Autowired
    AverageCurrencyRatesDAO averageCurrencyRatesDAO;

    /**
     * Post new currency rates here.
     *
     * @param newRates json that containing currency rates.
     * @return code 202 if everything is ok. Code 400 if something goes wrong.
     * @see CurrencyRatesDTO
     */
    @ApiOperation(value = "Отправить последние полученные котировки", nickname = "newRates",
            notes = "Сюда отправляются последние актуальные данные о котировках криптовалют.", tags = {"Rates",})
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted."),
            @ApiResponse(code = 400, message = "Bad request.")})
    @PostMapping("")
    public ResponseEntity<Object> gotNewRates(@ApiParam(value = "Новые котировки криптовалют как JSON объект.")
                                              @RequestBody List<CurrencyRatesDTO> newRates) {

        Map<String, Double> newRatesMap = Objects.requireNonNull(newRates).stream()
                .collect(Collectors.toMap(
                        CurrencyRatesDTO::getSymbol,
                        (CurrencyRatesDTO item) -> Double.parseDouble(item.getPrice())));
        logger.trace("Got new rates from api container. For example, BTC/RUB is: {}", newRatesMap.get("BTCRUB"));

        currentCurrencyRatesDAO.setCurrencyRates(newRatesMap);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Post new 1-minute average currency rates here.
     *
     * @param newRates json that containing currency rates.
     * @return code 202 if everything is ok. Code 400 if something goes wrong.
     * @see CurrencyRatesDTO
     */
    @ApiOperation(value = "Отправить средние значения котировок за 1 минуту", nickname = "avgRates",
            notes = "Сюда отправляются средние значения котировок криптовалют.", tags = {"Rates",})
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted."),
            @ApiResponse(code = 400, message = "Bad request.")})
    @PostMapping("/1m_avg")
    public ResponseEntity<Object> gotAvgRates(@ApiParam(value = "Новые котировки криптовалют как JSON объект.")
                                              @RequestBody List<CurrencyRatesDTO> newRates) {

        Map<String, Double> newRatesMap = Objects.requireNonNull(newRates).stream()
                .collect(Collectors.toMap(
                        CurrencyRatesDTO::getSymbol,
                        (CurrencyRatesDTO item) -> Double.parseDouble(item.getPrice())));
        logger.trace("Got new avg rates from api container. For example, BTC/RUB is: {}", newRatesMap.get("BTCRUB"));

        averageCurrencyRatesDAO.setCurrencyRates(newRatesMap);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}