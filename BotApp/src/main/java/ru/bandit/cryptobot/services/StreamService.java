package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.Rates1MinAverageRepository;
import ru.bandit.cryptobot.repositories.TriggerTypeRepository;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StreamService {

    @Autowired
    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    @Autowired
    Rates1MinAverageRepository rates1MinAverageRepository;

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    TriggerTypeRepository triggerTypeRepository;

    public Map<Long, List<String>> getStreams() {
        TriggerTypeEntity simpleTrigger = triggerTypeRepository.findByTriggerName("simple");
        TriggerTypeEntity averageTrigger = triggerTypeRepository.findByTriggerName("average");

        List<UserTriggerEntity> mailingList = userTriggersRepository.findByTriggerType(simpleTrigger);
        mailingList.addAll(userTriggersRepository.findByTriggerType(averageTrigger));

        Map<Long, List<String>> result = new HashMap<>();
        for (UserTriggerEntity stream : mailingList) {

            //if user is paused - ignore
            if (stream.getUser().isPaused()) continue;

            String currency1 = stream.getCurrencyPair().getCurrency1().getCurrencyNameUser();
            String currency2 = stream.getCurrencyPair().getCurrency2().getCurrencyNameUser();

            Long chatId = stream.getUser().getChatId();

            List<String> currentUserSubscriptions = result.get(chatId);
            if (currentUserSubscriptions == null) currentUserSubscriptions = new ArrayList<>();

            Double foundRate;
            if (stream.getTriggerType().equals(simpleTrigger)) {
                foundRate = currentCurrencyRatesDAO.getRateBySymbol(currency1 + currency2);
            } else {
                foundRate = rates1MinAverageRepository.findRatesBySymbol(currency1 + currency2);
            }

            String foundRateString;
            if (foundRate == null) foundRateString = "Нет данных";
            else foundRateString = foundRate.toString();

            if (stream.getTriggerType().equals(simpleTrigger)) {
                currentUserSubscriptions.add(String.format("%s/%s - %s",
                        currency1,
                        currency2,
                        foundRateString));
            } else {
                currentUserSubscriptions.add(String.format("%s/%s среднее за минуту - %s",
                        currency1,
                        currency2,
                        foundRateString));
            }

            result.put(chatId, currentUserSubscriptions);
        }
        return result;
    }
}
