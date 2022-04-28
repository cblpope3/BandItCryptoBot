package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.services.StreamService;

import java.util.List;
import java.util.Map;

/**
 * Class that handle exchange information with users via telegram.
 */
@Service
public class UserClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TelegramClient telegramClient;

    private final StreamService streamService;

    @Autowired
    public UserClient(TelegramClient telegramClient, StreamService streamService) {
        this.telegramClient = telegramClient;
        this.streamService = streamService;
    }

    /**
     * Method to send worked alarm trigger to user.
     *
     * @param userTrigger trigger that has been worked.
     * @param value       value of worked alarm trigger.
     */
    public void sendWorkedTargetTriggerToUser(UserTriggerEntity userTrigger, String value) {
        if (logger.isTraceEnabled()) logger.trace("Sending worked trigger #{} to user.", userTrigger.getId());

        SendMessage alarmMessage = new SendMessage();
        alarmMessage.setChatId(userTrigger.getUser().getChatId().toString());
        alarmMessage.setText(String.format("Сработал ваш будильник по валюте %s/%s! Текущий курс: %s.",
                userTrigger.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                userTrigger.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                value));
        telegramClient.sendMessage(alarmMessage);
    }

    /**
     * Method that sends subscriptions data to users.
     */
    @SuppressWarnings("unused")
    @Scheduled(fixedDelay = 5000)
    public void sendStreams() {

        Map<Long, List<String>> mailingList = streamService.getStreams();
        SendMessage sendMessage = new SendMessage();

        for (Map.Entry<Long, List<String>> stream : mailingList.entrySet()) {
            sendMessage.setChatId(stream.getKey().toString());
            sendMessage.setText(String.join("\n", stream.getValue()));
            telegramClient.sendMessage(sendMessage);
        }
    }
}
