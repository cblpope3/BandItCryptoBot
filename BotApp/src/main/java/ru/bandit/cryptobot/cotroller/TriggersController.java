package ru.bandit.cryptobot.cotroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trigger")
public class TriggersController {

    @PostMapping("/worked")
    public ResponseEntity<Object> triggerWorked() {
        //TODO this is dummy
        return new ResponseEntity<>("I'm not interested in triggers.", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshTriggers() {
        //TODO this is dummy
        return new ResponseEntity<>("I can't refresh triggers.", HttpStatus.NOT_IMPLEMENTED);
    }
}
