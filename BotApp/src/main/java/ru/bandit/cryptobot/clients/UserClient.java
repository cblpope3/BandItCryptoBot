package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.services.StreamService;
import ru.bandit.cryptobot.services.TriggersService;

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
    private final TriggersService triggersService;

    @Autowired
    public UserClient(TelegramClient telegramClient, StreamService streamService, TriggersService triggersService) {
        this.telegramClient = telegramClient;
        this.streamService = streamService;
        this.triggersService = triggersService;
    }

    /**
     * Method deletes worked alarm trigger from database and send it to user.
     *
     * @param triggerId if of trigger that has been worked.
     * @param value     value of worked alarm trigger.
     * @throws CommonBotAppException if trigger with given id is not found, or found trigger is not alarm type.
     */
    public void sendWorkedTargetTriggerToUser(Long triggerId, String value) throws CommonBotAppException {
        if (logger.isTraceEnabled()) logger.trace("Processing worked trigger #{}...", triggerId);

        UserTriggerEntity workedTrigger = triggersService.deleteWorkedTargetTrigger(triggerId);

        SendMessage alarmMessage = new SendMessage();
        alarmMessage.setChatId(workedTrigger.getUser().getChatId().toString());
        alarmMessage.setText(String.format("Сработал ваш будильник по валюте %s/%s! Текущий курс: %s.",
                workedTrigger.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                workedTrigger.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                value));

        telegramClient.sendMessage(alarmMessage);

        if (logger.isDebugEnabled())
            logger.debug("Worked trigger #{} successfully sent to user.", workedTrigger.getId());
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
