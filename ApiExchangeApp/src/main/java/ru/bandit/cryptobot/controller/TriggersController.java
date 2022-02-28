package ru.bandit.cryptobot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.dao.TriggersDAO;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.service.TriggersService;

/**
 * Controller to handle http requests for triggers management.
 */
@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    Logger logger = LoggerFactory.getLogger(TriggersController.class);

    @Autowired
    TriggersService triggersService;

    /**
     * Incoming request to add new trigger.
     *
     * @param trigger {@link TriggerDTO} as JSON object.
     * @return {@link HttpStatus} 202 if trigger added successfully,
     * {@link HttpStatus} 400 if JSON object format doesn't match {@link TriggersDAO} pattern.
     */
    @ApiOperation(value = "Добавить новый триггер.", nickname = "newTrigger",
            notes = "Добавление нового триггера для дальнейшего отслеживания.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted."),
            @ApiResponse(code = 400, message = "Bad request.")})
    @PostMapping("")
    public ResponseEntity<Object> addTrigger(@RequestBody TriggerDTO trigger) {

        if (logger.isTraceEnabled()) logger.trace("Got new trigger request: {}", trigger);

        if (trigger.getId() == null
                || trigger.getCurrencyPair() == null
                || trigger.getTargetValue() == null
                || trigger.getTriggerType() == null) {
            logger.warn("Received trigger have not valid format: {}", trigger);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        triggersService.addTrigger(trigger);

        if (logger.isDebugEnabled()) logger.debug("New trigger saved: {}", trigger);
        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    /**
     * Incoming request to delete trigger.
     *
     * @param triggerId {@link Long}: id of trigger to be removed.
     * @return {@link HttpStatus} 200 if trigger removed successfully,
     * {@link HttpStatus} 404 if requested trigger is not found in {@link TriggersDAO}.
     */
    @ApiOperation(value = "Удаление триггера.", nickname = "deleteTrigger",
            notes = "Удаление триггера с заданным id из списка отслеживаемых триггеров.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")})
    @DeleteMapping("{triggerId}")
    public ResponseEntity<Object> deleteTrigger(@ApiParam(value = "Id удаляемого триггера.")
                                                @PathVariable Long triggerId) {

        logger.debug("Got delete trigger #{} command.", triggerId);

        if (triggersService.deleteTrigger(triggerId)) {

            logger.trace("Trigger #{} removed successfully.", triggerId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            logger.warn("Trying to delete trigger #{}. Trigger not found.", triggerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
}
