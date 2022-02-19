package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.RatesRepository;
import ru.bandit.cryptobot.repositories.TriggerTypeRepository;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StreamService {

    @Autowired
    RatesRepository ratesRepository;

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    TriggerTypeRepository triggerTypeRepository;

    public Map<Long, List<String>> getStreams() {
        TriggerTypeEntity streamTrigger = triggerTypeRepository.findByTriggerName("simple");
        List<UserTriggerEntity> mailingList = userTriggersRepository.findByTriggerType(streamTrigger);

        Map<Long, List<String>> result = new HashMap<>();
        for (UserTriggerEntity stream : mailingList) {
            String currency1 = stream.getCurrencyPair().getCurrency1().getCurrencyNameUser();
            String currency2 = stream.getCurrencyPair().getCurrency2().getCurrencyNameUser();

            Long chatId = stream.getUser().getChatId();

            List<String> currentUserSubscriptions = result.get(chatId);
            if (currentUserSubscriptions == null) currentUserSubscriptions = new ArrayList<>();

            currentUserSubscriptions.add(String.format("%s/%s - %f",
                    currency1,
                    currency2,
                    ratesRepository.findRatesBySymbol(currency1 + currency2)));

            result.put(chatId, currentUserSubscriptions);
        }
        return result;
    }
}
