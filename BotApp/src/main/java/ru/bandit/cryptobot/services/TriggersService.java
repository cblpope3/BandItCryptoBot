package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.clients.TriggersClient;
import ru.bandit.cryptobot.clients.UserClient;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.dto.CurrencyPairDTO;
import ru.bandit.cryptobot.dto.TriggerDTO;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
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
    private static final String SIMPLE_TRIGGER_NAME = "simple";
    private static final String AVERAGE_TRIGGER_NAME = "average";
    private final Logger logger = LoggerFactory.getLogger(TriggersService.class);

    private final UserTriggersRepository userTriggersRepository;
    private final TriggerTypeRepository triggerTypeRepository;
    private final TriggersClient triggersClient;
    private final UserClient userClient;
    private final UsersService usersService;
    private final CurrencyService currencyService;
    private final CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    @Autowired
    public TriggersService(UserTriggersRepository userTriggersRepository,
                           TriggerTypeRepository triggerTypeRepository,
                           TriggersClient triggersClient,
                           UserClient userClient,
                           UsersService usersService,
                           CurrencyService currencyService,
                           CurrentCurrencyRatesDAO currentCurrencyRatesDAO) {

        this.userTriggersRepository = userTriggersRepository;
        this.triggerTypeRepository = triggerTypeRepository;
        this.triggersClient = triggersClient;
        this.userClient = userClient;
        this.usersService = usersService;
        this.currencyService = currencyService;
        this.currentCurrencyRatesDAO = currentCurrencyRatesDAO;
    }

    /**
     * This method allows getting target-up trigger type.
     *
     * @return target-up trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getUpTriggerType() {
        return triggerTypeRepository.findByTriggerName(TRIGGER_UP_NAME);
    }

    /**
     * This method allows getting target-down trigger type.
     *
     * @return target-down trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getDownTriggerType() {
        return triggerTypeRepository.findByTriggerName(TRIGGER_DOWN_NAME);
    }

    /**
     * This method allows getting simple stream trigger type.
     *
     * @return simple trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getSimpleTriggerType() {
        return triggerTypeRepository.findByTriggerName(SIMPLE_TRIGGER_NAME);
    }

    /**
     * This method allows getting average stream trigger type.
     *
     * @return average trigger type as {@link TriggerTypeEntity}.
     */
    public TriggerTypeEntity getAverageTriggerType() {
        return triggerTypeRepository.findByTriggerName(AVERAGE_TRIGGER_NAME);
    }

    /**
     * This method allows getting custom trigger type.
     *
     * @param triggerTypeName name of trigger type as written in {@link TriggerTypeEntity}.
     * @return requested trigger type as {@link TriggerTypeEntity}.
     * @throws CommonBotAppException if requested trigger type not found in database.
     */
    public TriggerTypeEntity getCustomTriggerType(String triggerTypeName) throws CommonBotAppException {
        TriggerTypeEntity triggerType = triggerTypeRepository.findByTriggerName(triggerTypeName.toLowerCase());
        if (triggerType == null) {
            logger.error("Trigger type #{} not found", triggerTypeName);
            throw new TriggerException("Not found trigger type.", TriggerException.ExceptionCause.NO_TRIGGER_TYPE);
        }
        return triggerType;
    }

    /**
     * This method allows creating new trigger type. For future use.
     *
     * @param triggerName        name of new trigger type.
     * @param triggerDescription description of new trigger type.
     */
    @SuppressWarnings("unused")
    public void createNewTriggerType(String triggerName, String triggerDescription) {
        if (logger.isTraceEnabled()) logger.trace("Creating new trigger type: {}.", triggerName);
        TriggerTypeEntity newTriggerType = new TriggerTypeEntity();
        newTriggerType.setTriggerName(triggerName.toLowerCase());
        newTriggerType.setTriggerDescription(triggerDescription);

        triggerTypeRepository.save(newTriggerType);
    }

    //=================================================
    //=================================================

    /**
     * Get currency rates once. Output format is: "BTC/EUR: 200.44"
     *
     * @param currencies requested rates currency pair.
     * @return user-friendly {@link String} with currency rates.
     * @throws CommonBotAppException if requested currency pair not found.
     */
    public String getOnce(CurrencyPairDTO currencies) throws CommonBotAppException {

        if (logger.isTraceEnabled()) logger.trace("Generating single request of {}/{} rates.",
                currencies.getCurrency1Name(), currencies.getCurrency2Name());

        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(currencies.getCurrenciesList());

        Double foundRate = currentCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameUser() +
                currencyPair.getCurrency2().getCurrencyNameUser());

        if (logger.isDebugEnabled()) logger.debug("Currency rate for {}/{} is found and returning to user.",
                currencyPair.getCurrency1().getCurrencyNameUser(),
                currencyPair.getCurrency2().getCurrencyNameUser());

        return String.format("%s/%s: %f",
                currencyPair.getCurrency1().getCurrencyNameUser(),
                currencyPair.getCurrency2().getCurrencyNameUser(),
                foundRate);
    }

    /**
     * Create new subscription for given user.
     *
     * @param user            {@link UserDTO} of user going to subscribe.
     * @param currencyPairDTO currency pair that involved in subscription.
     * @param triggerTypeName name of trigger type.
     * @param triggerValue    value of trigger. Nullable. Used only for alarm triggers.
     * @return saved trigger.
     * @throws CommonBotAppException if requested currency pair not found or user already have this subscription.
     */
    public UserTriggerEntity subscribe(UserDTO user, CurrencyPairDTO currencyPairDTO, String triggerTypeName, Integer triggerValue)
            throws CommonBotAppException {

        UserEntity foundUser = usersService.getUserEntity(user);
        CurrencyPairEntity currencyPair = currencyService.getCurrencyPair(currencyPairDTO.getCurrenciesList());

        TriggerTypeEntity triggerType = this.getCustomTriggerType(triggerTypeName);

        UserTriggerEntity newTrigger = new UserTriggerEntity();
        newTrigger.setUser(foundUser);
        newTrigger.setCurrencyPair(currencyPair);
        newTrigger.setTriggerType(triggerType);
        if (triggerValue != null) {
            newTrigger.setTargetValue(this.calculateAlarmTriggerValue(newTrigger, triggerValue));
        }

        //check if user already have this subscription
        if (this.doesTriggerExist(newTrigger)) {
            if (logger.isDebugEnabled()) logger.debug("User already subscribed to this trigger: {}.", newTrigger);
            throw new TriggerException("User already subscribed to this.",
                    TriggerException.ExceptionCause.ALREADY_SUBSCRIBED);
        }

        UserTriggerEntity savedTrigger = userTriggersRepository.save(newTrigger);
        if (logger.isDebugEnabled()) logger.debug("New trigger #{} saved.", savedTrigger.getId());

        //if new trigger is alarm, send it to api-app
        if (savedTrigger.getTriggerType().isAlarm()) {

            //alarm trigger must have target value
            if (newTrigger.getTargetValue() == null) {
                logger.error("Target trigger don't have target value.");
                throw new TriggerException("No target value.", TriggerException.ExceptionCause.TRIGGER_TYPE_NOT_MATCH);
            }
            this.sendTargetTriggerToApp(newTrigger);
        }

        return savedTrigger;
    }

    /**
     * Create simple or average subscription.
     *
     * @param user            {@link UserDTO} of user going to subscribe.
     * @param currencyPairDTO currency pair that involved in subscription.
     * @param triggerTypeName name of trigger type.
     * @return saved trigger.
     * @throws CommonBotAppException if requested currency pair not found or user already have this subscription.
     * @see #subscribe(UserDTO, CurrencyPairDTO, String, Integer)
     */
    public UserTriggerEntity subscribe(UserDTO user, CurrencyPairDTO currencyPairDTO, String triggerTypeName)
            throws CommonBotAppException {
        return this.subscribe(user, currencyPairDTO, triggerTypeName, null);
    }

    /**
     * Method calculates trigger target value with given currency pair, watching direction (target_up or target_down)
     * and watch value in percents.
     *
     * @param triggerTemplate trigger template with defined currency pair and trigger type.
     * @param valuePercents   value of alarm in percents.
     * @return target value of given currency pair rate.
     * @throws CommonBotAppException if no data about currency rates.
     */
    private Double calculateAlarmTriggerValue(UserTriggerEntity triggerTemplate, Integer valuePercents) throws CommonBotAppException {
        //get current currency pair rate
        Double currentRate = currencyService.getCurrentCurrencyRate(triggerTemplate.getCurrencyPair());

        double response;
        //choose direction
        if (triggerTemplate.getTriggerType().equals(this.getUpTriggerType())) {
            response = currentRate * (100 + valuePercents) / 100;
        } else if (triggerTemplate.getTriggerType().equals(this.getDownTriggerType())) {
            response = currentRate * (100 - valuePercents) / 100;
        } else {
            //unreachable statement
            logger.error("Unknown trigger type.");
            throw new IllegalArgumentException("Can't calculate target trigger value: unknown trigger type.");
        }

        if (logger.isTraceEnabled()) logger.trace("Calculated target trigger value: {}.", response);
        return response;
    }

    /**
     * Delete trigger with given id.
     *
     * @param user      {@link UserDTO} of user that requested subscription delete.
     * @param triggerId id of trigger to be deleted.
     * @throws CommonBotAppException if user don't have requested trigger.
     */
    public void unsubscribe(UserDTO user, Long triggerId) throws CommonBotAppException {
        if (logger.isTraceEnabled()) logger.trace("Trying to remove trigger #{}...", triggerId);

        UserTriggerEntity userTrigger = userTriggersRepository.findById(triggerId);
        if (userTrigger == null) {
            //trying to delete trigger that not exist
            if (logger.isDebugEnabled()) logger.debug("Trigger #{} doesn't exist.", triggerId);
            throw new TriggerException("User don't have this subscription.", TriggerException.ExceptionCause.SUBSCRIPTION_NOT_FOUND);
        } else if (!userTrigger.getUser().getUserId().equals(user.getUserId())) {
            //user trying to delete foreign trigger
            logger.warn("User #{} trying to delete foreign subscription #{}.", user.getUserId(), triggerId);
            throw new TriggerException("User don't have permission to access this trigger.",
                    TriggerException.ExceptionCause.SUBSCRIPTION_NOT_FOUND);
        } else {
            //everything is fine, deleting trigger
            if (userTrigger.getTriggerType().isAlarm()) {
                //if this is alarm trigger we must also delete it from trigger-app
                triggersClient.deleteTrigger(triggerId);
            }
            userTriggersRepository.delete(userTrigger);
            if (logger.isDebugEnabled()) logger.debug("Trigger #{} deleted successfully.", triggerId);
        }
    }

    /**
     * Remove all subscriptions for given user.
     *
     * @param user {@link UserDTO} of user that want to delete all subscriptions.
     * @throws CommonBotAppException if user don't have any subscriptions.
     */
    public void unsubscribeAll(UserDTO user) throws CommonBotAppException {

        UserEntity foundUser = usersService.getUserEntity(user);

        List<UserTriggerEntity> foundSubscriptions = userTriggersRepository.findByUser(foundUser);

        if (foundSubscriptions == null || foundSubscriptions.isEmpty()) {
            if (logger.isDebugEnabled()) logger.debug("Subscriptions for user #{} not found.", user.getUserId());
            throw new TriggerException("User don't have any subscription.", TriggerException.ExceptionCause.NO_SUBSCRIPTIONS);
        } else {
            for (UserTriggerEntity triggerEntity : foundSubscriptions) {
                if (triggerEntity.getTriggerType().isAlarm()) {
                    triggersClient.deleteTrigger(triggerEntity.getId());
                }
            }
            userTriggersRepository.deleteAll(foundSubscriptions);
            if (logger.isDebugEnabled())
                logger.debug("User #{} unsubscribed from all subscriptions successfully.", user.getUserId());
        }
    }

    /**
     * Get user-friendly subscriptions list for given user.
     *
     * @param user {@link UserDTO} of user that want to get subscriptions list.
     * @return user-friendly subscriptions list as {@link String}.
     * @throws CommonBotAppException if user don't have any subscriptions.
     */
    public String getAllSubscriptionsAsString(UserDTO user) throws CommonBotAppException {

        List<UserTriggerEntity> subscriptionsList = this.getAllUsersSubscriptions(user);

        return subscriptionsList.stream()
                .map(a -> {
                    if (a.getTriggerType().isAlarm()) {
                        return String.format("№%d - %s/%s - %s - %f",
                                a.getId(),
                                a.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                                a.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                                a.getTriggerType().getTriggerName(),
                                a.getTargetValue());
                    } else {
                        return String.format("№%d - %s/%s - %s",
                                a.getId(),
                                a.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                                a.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                                a.getTriggerType().getTriggerName());
                    }
                })
                .collect(Collectors.joining("\n"));

    }

    /**
     * Method returns all users subscriptions and alarms.
     *
     * @param user subscriptions owner.
     * @return {@link List} of all users subscriptions.
     * @throws CommonBotAppException if user don't have any subscriptions.
     */
    public List<UserTriggerEntity> getAllUsersSubscriptions(UserDTO user) throws CommonBotAppException {
        UserEntity foundUser = usersService.getUserEntity(user);
        List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(foundUser);
        if (subscriptionsList == null) {
            if (logger.isDebugEnabled()) logger.debug("Not found any subscription for user #{}.", user.getUserId());
            throw new TriggerException("User don't have any subscription.", TriggerException.ExceptionCause.NO_SUBSCRIPTIONS);
        } else {
            if (logger.isDebugEnabled()) logger.debug("Returning {} found subscriptions for user #{}.",
                    subscriptionsList.size(), user.getUserId());
            return subscriptionsList;
        }
    }

    /**
     * Get all existing target triggers.
     *
     * @return {@link List} of all existing target triggers as {@link TriggerDTO}.
     * @throws TriggerException if no alarm triggers found.
     */
    public List<TriggerDTO> getTargetTriggerDTOList() throws TriggerException {

        List<UserTriggerEntity> result = userTriggersRepository.findByTriggerType_IsAlarm(true);

        if (result.isEmpty()) {
            if (logger.isDebugEnabled()) logger.debug("No alarm triggers found in database.");
            throw new TriggerException("No alarm triggers found in database", TriggerException.ExceptionCause.NO_ALARM_TRIGGERS);
        }

        if (logger.isDebugEnabled()) logger.debug("Found {} alarm triggers in database.", result.size());

        return result.stream()
                .map(this::mapTriggerEntityToTriggerDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all stream-type triggers from users that aren't in pause mode.
     *
     * @return {@link List} of stream-type {@link UserTriggerEntity}.
     */
    public List<UserTriggerEntity> getAllActiveStreamTriggers() {
        List<UserTriggerEntity> result = new ArrayList<>();
        result.addAll(userTriggersRepository.findByTriggerTypeAndUser_IsPaused(getSimpleTriggerType(), false));
        result.addAll(userTriggersRepository.findByTriggerTypeAndUser_IsPaused(getAverageTriggerType(), false));
        return result;
    }

    /**
     * Send alarm of worked target trigger to user.
     *
     * @param triggerId id of worked trigger.
     * @param value     value of currency rates by the time trigger worked.
     * @throws CommonBotAppException if trigger is not found or trigger is not target type.
     */
    public void processWorkedTargetTrigger(Long triggerId, String value) throws CommonBotAppException {
        UserTriggerEntity workedTrigger = userTriggersRepository.findById(triggerId);
        if (workedTrigger == null) {
            logger.warn("Not found worked trigger #{} in database!", triggerId);
            throw new TriggerException("Not found requested trigger in database.",
                    TriggerException.ExceptionCause.SUBSCRIPTION_NOT_FOUND);
        } else {
            if (!workedTrigger.getTriggerType().isAlarm()) {
                logger.warn("Worked trigger #{} is not target trigger!", triggerId);
                throw new TriggerException("Requested trigger has other type.", TriggerException.ExceptionCause.TRIGGER_TYPE_NOT_MATCH);
            } else {
                userClient.sendWorkedTargetTriggerToUser(workedTrigger, value);
                userTriggersRepository.delete(workedTrigger);
                if (logger.isTraceEnabled())
                    logger.trace("Worked target trigger #{} with value={} processed successfully.", triggerId, value);
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

        //try-catch block works if TriggerDTO.TriggerType enum can't parse given trigger type.
        try {
            triggerDTO = new TriggerDTO(
                    userTriggerEntity.getId(),
                    userTriggerEntity.getCurrencyPair().getCurrency1().getCurrencyNameSource().toUpperCase() +
                            userTriggerEntity.getCurrencyPair().getCurrency2().getCurrencyNameSource().toUpperCase(),
                    userTriggerEntity.getTargetValue(),
                    TriggerDTO.TriggerType.valueOf(
                            userTriggerEntity.getTriggerType().getTriggerName().toUpperCase())
            );
        } catch (IllegalArgumentException e) {
            logger.error("{} is unknown trigger type: {}",
                    userTriggerEntity.getTriggerType().getTriggerName().toUpperCase(),
                    e.toString());
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
        if (logger.isTraceEnabled()) logger.trace("sending command to api-app: create trigger.");
        triggersClient.addNewTrigger(this.mapTriggerEntityToTriggerDTO(newTrigger));
    }
}