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

        //USDTRUB
        List<MailingListEntity> usdtRubMailingList = mailingListRepository.findByCurrency("USDTRUB");

        //BNBRUB
        List<MailingListEntity> bnbRubMailingList = mailingListRepository.findByCurrency("BNBRUB");

        //XRPRUB
        List<MailingListEntity> xrpRubMailingList = mailingListRepository.findByCurrency("XRPRUB");

        //ADARUB
        List<MailingListEntity> adaRubMailingList = mailingListRepository.findByCurrency("ADARUB");

        for (MailingListEntity entity : btcRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "BTC/RUB: " + newRates.getBtcRub().toString());
        }

        for (MailingListEntity entity : ethRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "ETH/RUB: " + newRates.getEthRub().toString());
        }

        for (MailingListEntity entity : usdtRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "USDT/RUB: " + newRates.getEthRub().toString());
        }

        for (MailingListEntity entity : bnbRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "BNB/RUB: " + newRates.getEthRub().toString());
        }

        for (MailingListEntity entity : xrpRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "XRP/RUB: " + newRates.getEthRub().toString());
        }

        for (MailingListEntity entity : adaRubMailingList) {
            bot.sendDataToSubscribers(entity.getChat().getChatName(), "ADA/RUB: " + newRates.getEthRub().toString());
        }


    }
}