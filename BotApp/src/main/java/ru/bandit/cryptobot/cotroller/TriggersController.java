package ru.bandit.cryptobot.cotroller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.clients.UserClient;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.TriggerException;
import ru.bandit.cryptobot.services.TriggersService;

import java.util.List;

/**
 * Class that handle http requests to /trigger address.
 */
@SuppressWarnings("unused")
@Api(tags = {"Triggers"}, value = "Triggers", description = "Triggers API")
@RestController
@RequestMapping("/trigger")
public class TriggersController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TriggersService triggersService;
    private final UserClient userClient;

    @Autowired
    public TriggersController(TriggersService triggersService, UserClient userClient) {
        this.triggersService = triggersService;
        this.userClient = userClient;
    }

    /**
     * Post worked alarm triggers here.
     *
     * @param triggerId id of worked trigger as stored in database.
     * @param value     last rate of corresponding currency pair.
     * @return code 200 if everything is ok, code 404 if worked trigger is not found in database.
     */
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

        if (logger.isDebugEnabled())
            logger.debug("Got new trigger POST request: trigger id = {}, value = {}", triggerId, value);

        try {
            userClient.sendWorkedTargetTriggerToUser(triggerId, value);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CommonBotAppException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Request all available alarm triggers list.
     *
     * @return code 200 and all alarm triggers list or code 204 if no alarm triggers found in database.
     */
    @ApiOperation(value = "Отправить запрос на обновление триггеров", nickname = "refreshTriggers",
            notes = "Сюда отправляется запрос на обновление триггеров от Api App.", tags = {"Triggers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK.", response = UserTriggerEntity.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "No content.")})
    @GetMapping("/getAllTarget")
    public ResponseEntity<List<TriggerDTO>> refreshTriggers() {

        if (logger.isDebugEnabled()) logger.debug("Got http request for triggers list.");

        try {
            List<TriggerDTO> response = triggersService.getTargetTriggerDTOList();
            if (logger.isTraceEnabled()) logger.trace("Returning triggers list as http response...");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (TriggerException e) {
            if (logger.isTraceEnabled()) logger.trace("Triggers list is empty.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
