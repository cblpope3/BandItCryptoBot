package ru.bandit.cryptobot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.data_containers.triggers.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.TriggersRepository;

@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    Logger logger = LoggerFactory.getLogger(TriggersController.class);

    @Autowired
    TriggersRepository triggersRepository;

    @ApiOperation(value = "Отправить новый триггер.", nickname = "newTrigger",
            notes = "Сюда отправляются новые триггеры для дальнейшего отслеживания.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @PostMapping("")
    public ResponseEntity<Object> addTrigger(@RequestBody UserTriggerEntity trigger) {

        triggersRepository.addTrigger(trigger);

        logger.debug("New trigger saved: {}", trigger.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Удалить триггер.", nickname = "deleteTrigger",
            notes = "Сюда отправляется запрос на удаление триггера.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @DeleteMapping("{triggerId}")
    public ResponseEntity<Object> deleteTrigger(@ApiParam(value = "Id удаляемого триггера.")
                                                @PathVariable Long triggerId) {
        triggersRepository.deleteTrigger(triggerId);

        logger.debug("Got delete trigger #{} command.", triggerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
