package ru.bandit.cryptobot.cotroller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.services.TriggersService;

import java.util.List;

@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    @Autowired
    TriggersService triggersService;

    @ApiOperation(value = "Отправить сработавшие триггеры", nickname = "workedTrigger",
            notes = "Сюда отправляется информация о сработавших триггерах для оповещения пользователей.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK."),
            @ApiResponse(code = 404, message = "Trigger not found.")})
    @PostMapping("/worked")
    public ResponseEntity<Object> triggerWorked(@ApiParam(value = "Id сработавшего триггера.")
                                                @RequestParam Long triggerId,
                                                @ApiParam(value = "Котировка валюты, триггер которой сработал.")
                                                @RequestParam String value) {

        if (triggersService.processTargetTrigger(triggerId, value)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @ApiOperation(value = "Отправить запрос на обновление триггеров", nickname = "refreshTriggers",
            notes = "Сюда отправляется запрос на обновление триггеров от Api App.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK.", response = UserTriggerEntity.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "No content.")})
    @GetMapping("/getAllTarget")
    public ResponseEntity<List<UserTriggerEntity>> refreshTriggers() {

        List<UserTriggerEntity> response = triggersService.getTargetTriggersList();

        if (response == null || response.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        else return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
