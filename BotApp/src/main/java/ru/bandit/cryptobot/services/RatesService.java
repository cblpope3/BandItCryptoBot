package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.bot.Bot;
import ru.bandit.cryptobot.data_containers.RatesJSONContainer;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;

@Service
public class RatesService {

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    Bot bot;

    public void sendNewRatesToSubscribers(RatesJSONContainer newRates) {
        //TODO must be totally reimplemented
//        //BTCRUB
//        List<UserTriggerEntity> btcRubMailingList = userTriggersRepository.findByCurrency("BTCRUB");
//        //ETHRUB
//        List<UserTriggerEntity> ethRubMailingList = userTriggersRepository.findByCurrency("ETHRUB");
//
//        //USDTRUB
//        List<UserTriggerEntity> usdtRubMailingList = userTriggersRepository.findByCurrency("USDTRUB");
//
//        //BNBRUB
//        List<UserTriggerEntity> bnbRubMailingList = userTriggersRepository.findByCurrency("BNBRUB");
//
//        //XRPRUB
//        List<UserTriggerEntity> xrpRubMailingList = userTriggersRepository.findByCurrency("XRPRUB");
//
//        //ADARUB
//        List<UserTriggerEntity> adaRubMailingList = userTriggersRepository.findByCurrency("ADARUB");
//
//        for (UserTriggerEntity entity : btcRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "BTC/RUB: " + newRates.getBtcRub().toString());
//        }
//
//        for (UserTriggerEntity entity : ethRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "ETH/RUB: " + newRates.getEthRub().toString());
//        }
//
//        for (UserTriggerEntity entity : usdtRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "USDT/RUB: " + newRates.getEthRub().toString());
//        }
//
//        for (UserTriggerEntity entity : bnbRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "BNB/RUB: " + newRates.getEthRub().toString());
//        }
//
//        for (UserTriggerEntity entity : xrpRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "XRP/RUB: " + newRates.getEthRub().toString());
//        }
//
//        for (UserTriggerEntity entity : adaRubMailingList) {
//            bot.sendDataToSubscribers(entity.getChat().getChatName(), "ADA/RUB: " + newRates.getEthRub().toString());
//        }
    }

}