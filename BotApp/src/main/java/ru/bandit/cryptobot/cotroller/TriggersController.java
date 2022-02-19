package ru.bandit.cryptobot.cotroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    @ApiOperation(value = "Отправить сработавшие триггеры", nickname = "workedTrigger",
            notes = "Сюда отправляется информация о сработавших триггерах для оповещения пользователей.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @PostMapping("/worked")
    public ResponseEntity<Object> triggerWorked() {
        //TODO this is dummy
        return new ResponseEntity<>("I'm not interested in triggers.", HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Отправить запрос на обновление триггеров", nickname = "refreshTriggers",
            notes = "Сюда отправляется запрос на обновление триггеров от Api App.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshTriggers() {
        //TODO this is dummy
        return new ResponseEntity<>("I can't refresh triggers.", HttpStatus.NOT_IMPLEMENTED);
    }
}
