package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.clients.TriggersClient;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.dto.QueryDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.TriggerException;
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

    private static final String TRIGGER_UP_NAME = "target_up";
    private static final String TRIGGER_DOWN_NAME = "target_down";
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

    @Autowired
    CurrencyService currencyService;

    @Autowired
    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

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
        return triggerTypeRepository.findByTriggerName(triggerTypeName.toLowerCase());
    }

    /**
     * This method allows creating new trigger type. For future use.
     *
     * @param triggerName        name of new trigger type.
     * @param triggerDescription description of new trigger type.
     */
    public void createNewTriggerType(String triggerName, String triggerDescription) {
        TriggerTypeEntity newTriggerType = new TriggerTypeEntity();
        newTriggerType.setTriggerName(triggerName.toLowerCase());
        newTriggerType.setTriggerDescription(triggerDescription);

        triggerTypeRepository.save(newTriggerType);
    }


    //=================================================
    //=================================================


    /**
     * Get currency rates once.
     *
     * @param params requested rates currency pair.
     * @return user-friendly {@link String} with currency rates.
     * @throws TriggerException if requested currency pair not found.
     */
    public String getOnce(QueryDTO params) throws TriggerException {

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(params.getCurrencies());

        if (currencyPair == null) {
            if (logger.isWarnEnabled())
                logger.warn("Requested currency pair #{} not found.", params.getCurrencies().toString());
            throw new TriggerException("No such currency pair.", TriggerException.ExceptionCause.NO_CURRENCY_PAIR);
        }

        return currentCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameUser() +
                currencyPair.getCurrency2().getCurrencyNameUser()).toString();
    }

    /**
     * Create new subscription for given user.
     *
     * @param user   {@link UserDTO} of user going to subscribe.
     * @param params request parameters as {@link QueryDTO}.
     * @throws TriggerException if requested currency pair not found or user already have this subscription.
     */
    public void subscribe(UserDTO user, QueryDTO params) throws TriggerException {
        UserEntity foundUser = usersService.getUserEntity(user);
        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(params.getCurrencies());
        if (currencyPair == null) {
            if (logger.isDebugEnabled()) logger.debug("User #{} requested subscription to wong currency pair: {}.",
                    user.getUserId(),
                    params.getCurrencies());
            throw new TriggerException("No such currency pair.", TriggerException.ExceptionCause.NO_CURRENCY_PAIR);
        }

        TriggerTypeEntity triggerType = this.getCustomTriggerType(params.getTriggerType());

        UserTriggerEntity newTrigger = new UserTriggerEntity();
        newTrigger.setUser(foundUser);
        newTrigger.setCurrencyPair(currencyPair);
        newTrigger.setTriggerType(triggerType);
        if (params.getTriggerParameter() != null) {
            newTrigger.setTargetValue(Integer.parseInt(params.getTriggerParameter()));
        }

        //check if user already have this subscription
        if (this.doesTriggerExist(newTrigger)) {
            logger.debug("User already subscribed to this trigger");
            throw new TriggerException("User already subscribed to this.",
                    TriggerException.ExceptionCause.ALREADY_SUBSCRIBED);
        }

        userTriggersRepository.save(newTrigger);

        //if new trigger is alarm, send it to api-app
        if (newTrigger.getTriggerType().equals(this.getTriggerUp()) ||
                newTrigger.getTriggerType().equals(this.getTriggerDown())) {
            this.sendTargetTriggerToApp(newTrigger);
        }
    }


    /**
     * Delete trigger with given id.
     *
     * @param user      {@link UserDTO} of user that requested subscription delete.
     * @param triggerId id of trigger to be deleted.
     * @throws TriggerException if user don't have requested trigger.
     */
    public void unsubscribe(UserDTO user, Long triggerId) throws TriggerException {
        logger.trace("Trying to remove trigger #{}", triggerId);

        UserTriggerEntity userTrigger = userTriggersRepository.findById(triggerId);
        if (userTrigger == null) {
            //trying to delete trigger that not exist
            logger.trace("Trigger #{} doesn't exist.", triggerId);
            throw new TriggerException("User don't have this subscription.", TriggerException.ExceptionCause.SUBSCRIPTION_NOT_FOUND);
        } else if (!userTrigger.getUser().getUserId().equals(user.getUserId())) {
            //user trying to delete foreign trigger
            //todo check how this works
            logger.warn("User #{} trying to delete foreign subscription #{}.", user.getUserId(), triggerId);
            throw new TriggerException("User don't have permission to access this trigger.",
                    TriggerException.ExceptionCause.SUBSCRIPTION_NOT_FOUND);
        } else {
            //everything is fine, deleting trigger
            if (userTrigger.getTriggerType().equals(this.getTriggerUp()) ||
                    userTrigger.getTriggerType().equals(this.getTriggerDown())) {
                triggersClient.deleteTrigger(triggerId);
            }
            userTriggersRepository.delete(userTrigger);
            logger.trace("Trigger #{} deleted successfully.", triggerId);
        }
    }

    /**
     * Remove all subscriptions for given user.
     *
     * @param user {@link UserDTO} of user that want to delete all subscriptions.
     * @throws TriggerException if user don't have any subscriptions.
     */
    public void unsubscribeAll(UserDTO user) throws TriggerException {

        UserEntity foundUser = usersService.getUserEntity(user);

        List<UserTriggerEntity> foundSubscriptions = userTriggersRepository.findByUser(foundUser);

        if (foundSubscriptions == null || foundSubscriptions.isEmpty()) {
            logger.debug("Subscriptions for user #{} not found.", user.getUserId());
            throw new TriggerException("User don't have any subscription.", TriggerException.ExceptionCause.NO_SUBSCRIPTIONS);
        } else {
            for (UserTriggerEntity triggerEntity : foundSubscriptions) {
                if (triggerEntity.getTriggerType().equals(this.getTriggerUp()) ||
                        triggerEntity.getTriggerType().equals(this.getTriggerDown())) {
                    triggersClient.deleteTrigger(triggerEntity.getId());
                }
            }
            userTriggersRepository.deleteAll(foundSubscriptions);
            logger.debug("User #{} unsubscribed from all subscriptions successfully.", user.getUserId());
        }
    }

    /**
     * Get user-friendly subscriptions list for given user.
     *
     * @param user {@link UserDTO} of user that want to get subscriptions list.
     * @return user-friendly subscriptions list as {@link String}.
     * @throws TriggerException if user don't have any subscriptions.
     */
    public String getAllSubscriptionsAsString(UserDTO user) throws TriggerException {
        UserEntity foundUser = usersService.getUserEntity(user);

        List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(foundUser);
        if (subscriptionsList == null) {
            logger.debug("Not found any triggers for user #{}.", user.getUserId());
            throw new TriggerException("User don't have any subscription.", TriggerException.ExceptionCause.NO_SUBSCRIPTIONS);
        } else {
            logger.trace("Returned subscriptions of user #{}", user.getUserId());
            return subscriptionsList.stream()
                    //FIXME subscription name is not fine for simple triggers
                    .map(a -> String.format("№%d - %s/%s - %s - %d",
                            a.getId(),
                            a.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                            a.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                            a.getTriggerType().getTriggerName(),
                            a.getTargetValue()))
                    .collect(Collectors.joining("\n"));
        }

    }

    /**
     * Get all existing target triggers.
     *
     * @return {@link List} of all existing target triggers as {@link TriggerDTO}.
     */
    public List<TriggerDTO> getTargetTriggerDTOList() {

        List<UserTriggerEntity> result = new ArrayList<>();
        result.addAll(userTriggersRepository.findByTriggerType(getTriggerUp()));
        result.addAll(userTriggersRepository.findByTriggerType(getTriggerDown()));

        return result.stream()
                .map(this::mapTriggerEntityToTriggerDTO)
                .collect(Collectors.toList());
    }

    /**
     * Send alarm of worked target trigger to user.
     *
     * @param triggerId id of worked trigger.
     * @param value     value of currency rates by the time trigger worked.
     * @return true if trigger processed successfully. False if trigger not found or found trigger is not target-type.
     */
    public boolean processWorkedTargetTrigger(Long triggerId, String value) {
        UserTriggerEntity workedTrigger = userTriggersRepository.findById(triggerId);
        if (workedTrigger == null) {
            logger.warn("Not found worked trigger #{} in database!", triggerId);
            return false;
        } else {
            if (!workedTrigger.getTriggerType().equals(this.getTriggerUp()) &&
                    !workedTrigger.getTriggerType().equals(this.getTriggerDown())) {
                logger.warn("Worked trigger #{} is not target trigger!", triggerId);
                return false;
            } else {
                bot.sendWorkedTargetTriggerToUser(workedTrigger, value);
                userTriggersRepository.delete(workedTrigger);
                if (logger.isTraceEnabled())
                    logger.trace("Worked target trigger #{} with value={} processed successfully.", triggerId, value);
                return true;
            }
        }
    }

    /**
     * Method creates {@link TriggerDTO} object that equals given {@link UserTriggerEntity} object.
     *
     * @param userTriggerEntity original {@link UserTriggerEntity} object to be mapped.
     * @return mapped {@link TriggerDTO} object if triggerType is fine. Null if triggerType is not found in database.
     */
    private TriggerDTO mapTriggerEntityToTriggerDTO(UserTriggerEntity userTriggerEntity) {
        TriggerDTO triggerDTO;

        try {
            triggerDTO = new TriggerDTO(
                    userTriggerEntity.getId(),
                    userTriggerEntity.getCurrencyPair().getCurrency1().getCurrencyNameSource().toUpperCase() +
                            userTriggerEntity.getCurrencyPair().getCurrency2().getCurrencyNameSource().toUpperCase(),
                    //fixme this value in database must be double
                    userTriggerEntity.getTargetValue().doubleValue(),
                    TriggerDTO.TriggerType.valueOf(
                            userTriggerEntity.getTriggerType().getTriggerName().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            logger.error("Unknown trigger type: {}", e.toString());
            triggerDTO = null;
        }

        return triggerDTO;
    }

    /**
     * Check if user already have subscription.
     *
     * @param trigger {@link UserTriggerEntity} that we want to check.
     * @return true if user already have this subscription.
     */
    private boolean doesTriggerExist(UserTriggerEntity trigger) {
        //getting list of triggers with matching userId, triggerTypeId, currencyPairId and targetValue
        List<UserTriggerEntity> existingTriggerList = userTriggersRepository
                .findByUserAndTriggerTypeAndCurrencyPairAndTargetValue(trigger.getUser(), trigger.getTriggerType(),
                        trigger.getCurrencyPair(), trigger.getTargetValue());

        //if this list is null or empty, trigger not exist
        return existingTriggerList != null && !existingTriggerList.isEmpty();
    }


    /**
     * Method sends target trigger to Trigger-App.
     *
     * @param newTrigger new {@link UserTriggerEntity} to send to Trigger-App.
     */
    private void sendTargetTriggerToApp(UserTriggerEntity newTrigger) {
        logger.debug("sending command to api-app: create trigger.");
        triggersClient.addNewTrigger(this.mapTriggerEntityToTriggerDTO(newTrigger));
    }
}