package ru.bandit.cryptobot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trigger")
public class TriggersController {

    @PostMapping("")
    public ResponseEntity<Object> addTrigger() {
        //TODO implement
        return new ResponseEntity<>("I can not add new trigger.", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshTriggers() {
        //TODO implement
        return new ResponseEntity<>("I can not refresh triggers.", HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteTrigger() {
        //TODO implement
        return new ResponseEntity<>("I can not delete triggers.", HttpStatus.NOT_IMPLEMENTED);
    }
}
