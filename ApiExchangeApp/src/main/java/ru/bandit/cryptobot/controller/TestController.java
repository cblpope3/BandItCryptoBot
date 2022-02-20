package ru.bandit.cryptobot.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.service.TestService;

@Api(tags = {"Test"}, value = "Test", description = "Test controller to ping application")
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService testService;

    @ApiOperation(value = "Тестовый контроллер", nickname = "test",
            notes = "Сюда отправляются бессмысленные тестовые запросы.", tags = {"Test",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok.")})
    @GetMapping("")
    @ResponseBody
    public String getTestPage(@ApiParam(value = "Параметр, который сервис отправит в ответ. Если оставить пустым - ответ будет \"ОК\".")
                                  @RequestParam(required = false) String message) {
        return testService.getTestMessage(message);
    }
}