package ru.leonov.testbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.leonov.testbot.service.TestService;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("")
    @ResponseBody
    public String getTestPage(@RequestParam(required = false) String message) {
        return testService.getTestMessage(message);
    }

    @GetMapping("/ui")
    public String getTestGui(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", testService.getTestMessage(message));
        return "test";
    }
}