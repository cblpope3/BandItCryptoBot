package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.entities.RatesJSONContainer;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.List;

@Service
public class RatesService {

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    Bot bot;

    public void sendNewRatesToSubscribers(RatesJSONContainer newRates) {
        //TODO must be totally reimplemented
        //BTCRUB
        List<MailingListEntity> btcRubMailingList = mailingListRepository.findByCurrency("BTCRUB");
        //ETHRUB
        List<MailingListEntity> ethRubMailingList = mailingListRepository.findByCurrency("ETHRUB");

        for (MailingListEntity entity : btcRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "BTCRUB: " + newRates.getBtcRub().toString());
        }

        for (MailingListEntity entity : ethRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "ETHRUB: " + newRates.getEthRub().toString());
        }
    }
}