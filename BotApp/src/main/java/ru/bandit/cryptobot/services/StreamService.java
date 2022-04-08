package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This service implements methods that handle mailing new currency rates to users.
 *
 * @see #getStreams()
 */
@Service
public class StreamService {

    TriggersService triggersService;

    CurrencyService currencyService;

    @Autowired
    public StreamService(TriggersService triggersService, CurrencyService currencyService) {
        this.triggersService = triggersService;
        this.currencyService = currencyService;
    }

    /**
     * Method making user-friendly information about user subscriptions.
     *
     * @return {@link Map}, where key is user id, value is {@link List} of user-friendly {@link String} with
     * information about currency rates that can be published directly to user.
     */
    public Map<Long, List<String>> getStreams() {

        //getting all active triggers list from database
        List<UserTriggerEntity> mailingList = triggersService.getAllActiveStreamTriggers();

        return mailingList.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getUser().getUserId(),
                        Collector.of(
                                ArrayList::new,
                                (result, userTrigger) -> result.add(this.getUserFriendlyCurrencyRate(userTrigger)),
                                (result1, result2) -> {
                                    result1.addAll(result2);
                                    return result1;
                                }
                        )
                ));
    }

    /**
     * Make user-friendly currency rate from {@link UserTriggerEntity}.
     *
     * @param userTrigger stream trigger to be mapped.
     * @return user-friendly currency rate with currencies symbols.
     */
    private String getUserFriendlyCurrencyRate(UserTriggerEntity userTrigger) {
        //todo specify currencies refreshing time
        if (userTrigger.getTriggerType().equals(triggersService.getSimpleTriggerType())) {
            return String.format("%s - %s. Обновлено: ---",
                    this.getUserFriendlyCurrencyPair(userTrigger.getCurrencyPair()),
                    currencyService.getCurrentCurrencyRate(userTrigger.getCurrencyPair()));
        } else if (userTrigger.getTriggerType().equals(triggersService.getAverageTriggerType())) {
            return String.format("%s среднее за минуту - %s. Обновлено: ---",
                    this.getUserFriendlyCurrencyPair(userTrigger.getCurrencyPair()),
                    currencyService.getAverageCurrencyRate(userTrigger.getCurrencyPair()));
        } else {
            throw new IllegalArgumentException("Unknown user trigger type.");
        }
    }


    /**
     * Get user-friendly currency pair name.
     *
     * @param currencyPair requested currency pair.
     * @return user-friendly currency pair.
     * @see CurrencyPairEntity
     * @see String
     */
    private String getUserFriendlyCurrencyPair(CurrencyPairEntity currencyPair) {
        return String.format("%s/%s",
                currencyPair.getCurrency1().getCurrencyNameUser(),
                currencyPair.getCurrency2().getCurrencyNameUser());
    }
}
