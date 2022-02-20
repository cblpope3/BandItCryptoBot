package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.TriggerTypeRepository;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TriggersService {

    Logger logger = LoggerFactory.getLogger(TriggersService.class);
    @Autowired
    UserTriggersRepository userTriggersRepository;
    @Autowired
    TriggerTypeRepository triggerTypeRepository;
    @Autowired
    Bot bot;
    @Value("${api-app.hostname}")
    private String apiAppCurrencyUrl;

    public List<UserTriggerEntity> getTargetTriggersList() {
        TriggerTypeEntity targetUpType = triggerTypeRepository.findByTriggerName("target-up");
        TriggerTypeEntity targetDownType = triggerTypeRepository.findByTriggerName("target-down");

        List<UserTriggerEntity> result = new ArrayList<>();
        result.addAll(userTriggersRepository.findByTriggerType(targetUpType));
        result.addAll(userTriggersRepository.findByTriggerType(targetDownType));
        return result;
    }

    public boolean processTargetTrigger(Long triggerId, String value) {
        UserTriggerEntity workedTrigger = userTriggersRepository.findById(triggerId);
        if (workedTrigger == null) {
            logger.warn("Not found worked trigger #{} in database!", triggerId);
            return false;
        } else {
            if (!workedTrigger.getTriggerType().getTriggerName().equals("target-up") &&
                    !workedTrigger.getTriggerType().getTriggerName().equals("target-down")) {
                logger.warn("Worked target trigger is not target trigger!");
                return false;
            } else {
                logger.trace("Worked target trigger processed successfully!");
                bot.sendWorkedTargetTriggerToUser(workedTrigger, value);
                userTriggersRepository.delete(workedTrigger);
                return true;
            }
        }
    }

    public void deleteTargetTrigger(Long triggerId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(apiAppCurrencyUrl + "trigger/" + triggerId.toString());
        } catch (RestClientException e) {
            logger.warn("Exception during sending 'delete trigger #{}' request to api-app by address {}trigger/{}: {}",
                    triggerId,
                    apiAppCurrencyUrl,
                    triggerId,
                    e.getMessage());
        }

        logger.debug("sending command to api-app: delete trigger #{}.", triggerId);
    }

    public void createTargetTrigger(UserTriggerEntity newTrigger) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(apiAppCurrencyUrl + "trigger", newTrigger, UserTriggerEntity.class);
        } catch (RestClientException e) {
            logger.warn("Exception during sending 'create new trigger' request to api-app by address {}trigger/: {}",
                    apiAppCurrencyUrl,
                    e.getMessage());
        }

        logger.debug("sending command to api-app: create trigger.");
    }
}
