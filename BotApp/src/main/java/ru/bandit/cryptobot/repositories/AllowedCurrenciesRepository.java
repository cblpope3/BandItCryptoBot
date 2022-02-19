package ru.bandit.cryptobot.repositories;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllowedCurrenciesRepository {
    //TODO change this repository to database
    List<String> allowedCurrenciesList = List.of("RUB", "USD", "EUR");
    List<String> allowedCryptoList = List.of("BTC", "ETH", "USDT", "BNB", "XRP", "ADA");

    public List<String> findAllCurrencies(){
        return allowedCurrenciesList;
    }

    public List<String> findAllCrypto(){
        return allowedCryptoList;
    }
}