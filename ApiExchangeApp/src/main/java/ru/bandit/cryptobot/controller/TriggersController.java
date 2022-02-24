package ru.bandit.cryptobot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;
import ru.bandit.cryptobot.DAO.TriggersDAO;

@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    Logger logger = LoggerFactory.getLogger(TriggersController.class);

    @Autowired
    TriggersDAO triggersDAO;

    @ApiOperation(value = "Добавить новый триггер.", nickname = "newTrigger",
            notes = "Добавление нового триггера для дальнейшего отслеживания.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK.")})
    @PostMapping("")
    public ResponseEntity<Object> addTrigger(@RequestBody UserTriggerEntity trigger) {

        triggersDAO.addTrigger(trigger);

        if (logger.isDebugEnabled()) logger.debug("New trigger saved: {}", trigger.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Удаление триггера.", nickname = "deleteTrigger",
            notes = "Удаление триггера с заданным id из списка отслеживаемых триггеров.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 404, message = "Not found.")})
    @DeleteMapping("{triggerId}")
    public ResponseEntity<Object> deleteTrigger(@ApiParam(value = "Id удаляемого триггера.")
                                                @PathVariable Long triggerId) {
        logger.debug("Got delete trigger #{} command.", triggerId);
        if (triggersDAO.deleteTrigger(triggerId)) {
            logger.trace("Trigger #{} removed successfully.", triggerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.warn("Trying to delete trigger #{}. Trigger not found.", triggerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
