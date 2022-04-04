package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.clients.TriggersClient;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.TriggerTypeRepository;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service implements interaction with triggers and trigger types.
 */
@Service
public class TriggersService {

    private static final String TRIGGER_UP_NAME = "target-up";
    private static final String TRIGGER_DOWN_NAME = "target-down";
    private final Logger logger = LoggerFactory.getLogger(TriggersService.class);
    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    TriggerTypeRepository triggerTypeRepository;

    @Autowired
    Bot bot;

    @Autowired
    TriggersClient triggersClient;

    @Autowired
    UsersService usersService;

    //todo decide if this needed here. better move to clients class.
    @Value("${api-app.hostname}")
    private String apiAppCurrencyUrl;

    /**
     * This method allows getting target-up trigger type.
     *
     * @return target-up trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getTriggerUp() {
        return triggerTypeRepository.findByTriggerName(TRIGGER_UP_NAME);
    }

    /**
     * This method allows getting target-down trigger type.
     *
     * @return target-down trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getTriggerDown() {
        return triggerTypeRepository.findByTriggerName(TRIGGER_DOWN_NAME);
    }

    /**
     * This method allows getting custom trigger type.
     *
     * @param triggerTypeName name of trigger type as written in {@link TriggerTypeEntity}.
     * @return requested trigger type as {@link TriggerTypeEntity}, or null if not found.
     */
    public TriggerTypeEntity getCustomTriggerType(String triggerTypeName) {
        return triggerTypeRepository.findByTriggerName(triggerTypeName);
    }

    /**
     * This method allows creating new trigger type. For future use.
     *
     * @param triggerName        name of new trigger type.
     * @param triggerDescription description of new trigger type.
     */
    public void createNewTriggerType(String triggerName, String triggerDescription) {
        TriggerTypeEntity newTriggerType = new TriggerTypeEntity();
        newTriggerType.setTriggerName(triggerName);
        newTriggerType.setTriggerDescription(triggerDescription);

        triggerTypeRepository.save(newTriggerType);
    }

    public List<TriggerDTO> getTargetTriggersList() {
        TriggerTypeEntity targetUpType = getTriggerUp();
        TriggerTypeEntity targetDownType = getTriggerDown();

        List<UserTriggerEntity> result = new ArrayList<>();
        result.addAll(userTriggersRepository.findByTriggerType(targetUpType));
        result.addAll(userTriggersRepository.findByTriggerType(targetDownType));

        return result.stream().map(
                        trigger -> {
                            TriggerDTO triggerDTO = new TriggerDTO();
                            triggerDTO.setId(trigger.getId());
                            //fixme this must be double in database
                            triggerDTO.setTargetValue(trigger.getTargetValue().doubleValue());
                            if (trigger.getTriggerType().getTriggerName().equalsIgnoreCase(TRIGGER_UP_NAME)) {
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
            if (!workedTrigger.getTriggerType().getTriggerName().equals(TRIGGER_UP_NAME) &&
                    !workedTrigger.getTriggerType().getTriggerName().equals(TRIGGER_DOWN_NAME)) {
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
        triggersClient.deleteTrigger(triggerId);

        logger.debug("sending command to api-app: delete trigger #{}.", triggerId);
    }

    public void createTargetTrigger(UserTriggerEntity newTrigger) {

        TriggerDTO triggerDTO = new TriggerDTO();
        triggerDTO.setId(newTrigger.getId());
        //fixme this value in database must be double
        triggerDTO.setTargetValue(newTrigger.getTargetValue().doubleValue());
        triggerDTO.setCurrencyPair(newTrigger.getCurrencyPair().getCurrency1().getCurrencyNameSource().toUpperCase() +
                newTrigger.getCurrencyPair().getCurrency2().getCurrencyNameSource().toUpperCase());
        if (newTrigger.getTriggerType().getTriggerName().equalsIgnoreCase(TRIGGER_UP_NAME)) {
            triggerDTO.setTriggerType(TriggerDTO.TriggerType.UP);
        } else {
            triggerDTO.setTriggerType(TriggerDTO.TriggerType.DOWN);
        }

        triggersClient.addNewTrigger(triggerDTO);

        logger.debug("sending command to api-app: create trigger.");
    }
}
