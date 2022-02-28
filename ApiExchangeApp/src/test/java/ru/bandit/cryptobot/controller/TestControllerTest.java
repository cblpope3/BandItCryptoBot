package ru.bandit.cryptobot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bandit.cryptobot.service.TestService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ru.bandit.cryptobot.controller.TestController.class)
class TestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TestService testService;

    //testing empty request
    @Test
    void getTestPage() throws Exception {

        when(testService.getTestMessage(null)).thenReturn("OK");
        this.mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }

    //testing request with params
    @Test
    void getTestPageWithParams() throws Exception {

        when(testService.getTestMessage("Test")).thenReturn("Test");
        this.mockMvc.perform(get("/test?message=Test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test")));
    }
}