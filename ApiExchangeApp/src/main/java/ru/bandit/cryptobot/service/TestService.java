package ru.bandit.cryptobot.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String getTestMessage(String message) {
        if (message == null) return "OK";
        else return message;
    }
}