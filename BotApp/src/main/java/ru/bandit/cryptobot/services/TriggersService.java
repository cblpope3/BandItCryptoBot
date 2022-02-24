package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.DTO.TriggerDTO;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.TriggerTypeRepository;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<TriggerDTO> getTargetTriggersList() {
        TriggerTypeEntity targetUpType = triggerTypeRepository.findByTriggerName("target-up");
        TriggerTypeEntity targetDownType = triggerTypeRepository.findByTriggerName("target-down");

        List<UserTriggerEntity> result = new ArrayList<>();
        result.addAll(userTriggersRepository.findByTriggerType(targetUpType));
        result.addAll(userTriggersRepository.findByTriggerType(targetDownType));

        return result.stream().map(
                        trigger -> {
                            TriggerDTO triggerDTO = new TriggerDTO();
                            triggerDTO.setId(trigger.getId());
                            //fixme this must be double in database
                            triggerDTO.setTargetValue(trigger.getTargetValue().doubleValue());
                            if (trigger.getTriggerType().getTriggerName().toLowerCase() == "target-up") {
                                triggerDTO.setTriggerType(TriggerDTO.TriggerType.UP);
                            } else {
                                triggerDTO.setTriggerType(TriggerDTO.TriggerType.DOWN);
                            }
                            triggerDTO.setCurrencyPair(trigger.getCurrencyPair().getCurrency1().getCurrencyNameSource() +
                                    trigger.getCurrencyPair().getCurrency2().getCurrencyNameSource());
                            return triggerDTO;
                        }
                )
                .collect(Collectors.toList());
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

        TriggerDTO triggerDTO = new TriggerDTO();
        triggerDTO.setId(newTrigger.getId());
        //fixme this value in database must be double
        triggerDTO.setTargetValue(newTrigger.getTargetValue().doubleValue());
        triggerDTO.setCurrencyPair(newTrigger.getCurrencyPair().getCurrency1().getCurrencyNameSource().toUpperCase() +
                newTrigger.getCurrencyPair().getCurrency2().getCurrencyNameSource().toUpperCase());
        if (newTrigger.getTriggerType().getTriggerName().toLowerCase() == "target-up") {

        } else {
            triggerDTO.setTriggerType(TriggerDTO.TriggerType.DOWN);
        }

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(apiAppCurrencyUrl + "trigger", triggerDTO, TriggerDTO.class);
        } catch (RestClientException e) {
            logger.warn("Exception during sending 'create new trigger' request to api-app by address {}trigger/: {}",
                    apiAppCurrencyUrl,
                    e.getMessage());
        }

        logger.debug("sending command to api-app: create trigger.");
    }
}
