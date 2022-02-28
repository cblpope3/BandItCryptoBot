package ru.bandit.cryptobot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.bandit.cryptobot.service.TriggersService;
import ru.bandit.cryptobot.test_data.ClientsTestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ru.bandit.cryptobot.controller.TriggersController.class)
class TriggersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TriggersService triggersService;

    //test fine add trigger request
    @Test
    void addTrigger() throws Exception {

        this.mockMvc.perform(post("/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClientsTestData.getTriggerJson()))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(triggersService, times(1)).addTrigger(ClientsTestData.testTrigger1);
    }

    //test add trigger request with missing parameters
    @Test
    void addTriggerNoParams() throws Exception {

        this.mockMvc.perform(post("/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClientsTestData.getWrongTriggerJson1()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post("/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClientsTestData.getWrongTriggerJson2()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post("/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClientsTestData.getWrongTriggerJson3()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post("/trigger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClientsTestData.getWrongTriggerJson4()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(triggersService, times(0)).addTrigger(any());
    }

    //testing fine delete trigger request
    @Test
    void deleteTrigger() throws Exception {

        when(triggersService.deleteTrigger(ClientsTestData.trigger1Id)).thenReturn(true);

        this.mockMvc.perform(delete("/trigger/" + ClientsTestData.trigger1Id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(triggersService, times(1)).deleteTrigger(ClientsTestData.trigger1Id);
    }

    //testing request to delete not existing trigger
    @Test
    void deleteTriggerNotFound() throws Exception {

        when(triggersService.deleteTrigger(ClientsTestData.triggerIdWrong)).thenReturn(false);

        this.mockMvc.perform(delete("/trigger/" + ClientsTestData.triggerIdWrong))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(triggersService, times(1)).deleteTrigger(ClientsTestData.triggerIdWrong);
    }

    //testing null request to delete trigger
    @Test
    void deleteTriggerNoParams() throws Exception {

        this.mockMvc.perform(delete("/trigger/"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());

        verify(triggersService, times(0)).deleteTrigger(any());
    }
}