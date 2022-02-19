package ru.bandit.cryptobot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    @ApiOperation(value = "Отправить новый триггер.", nickname = "newTrigger",
            notes = "Сюда отправляются новые триггеры для дальнейшего отслеживания.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @PostMapping("")
    public ResponseEntity<Object> addTrigger() {
        //TODO implement
        return new ResponseEntity<>("I can not add new trigger.", HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Отправить все триггеры.", nickname = "refreshTriggers",
            notes = "Сюда отправляются все существующие триггеры для синхронизации приложений.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshTriggers() {
        //TODO implement
        return new ResponseEntity<>("I can not refresh triggers.", HttpStatus.NOT_IMPLEMENTED);
    }

    @ApiOperation(value = "Удалить триггер.", nickname = "deleteTrigger",
            notes = "Сюда отправляется запрос на удаление триггера.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 501, message = "Not implemented.")})
    @DeleteMapping("")
    public ResponseEntity<Object> deleteTrigger() {
        //TODO implement
        return new ResponseEntity<>("I can not delete triggers.", HttpStatus.NOT_IMPLEMENTED);
    }
}
