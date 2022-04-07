package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.repositories.CurrencyPairRepository;
import ru.bandit.cryptobot.repositories.CurrencyRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This service implements interaction with currencies and currency pairs.
 */
@Service
public class CurrencyService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    CurrencyRepository currencyRepository;

    CurrencyPairRepository currencyPairRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyPairRepository currencyPairRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyPairRepository = currencyPairRepository;
    }

    /**
     * Get currency from database by its name.
     *
     * @param symbol currency name (for example "RUB", or "BTC").
     * @return found currency as {@link CurrencyEntity}. If requested currency not found in database return null.
     */
    public CurrencyEntity getCurrencyBySymbol(String symbol) {
        CurrencyEntity foundCurrency = currencyRepository.findByCurrencyNameUser(symbol.toUpperCase());
        if (foundCurrency == null) logger.warn("Requested currency {} not found!", symbol);
        return foundCurrency;
    }

    /**
     * Get all currencies stored in database.
     *
     * @return {@link Set} of {@link CurrencyEntity}.
     */
    public Set<CurrencyEntity> getAllCurrencies() {
        Set<CurrencyEntity> resultSet = new HashSet<>(currencyRepository.findAll());
        if (resultSet.isEmpty()) logger.warn("Not found any currency in database!");
        return resultSet;
    }

    /**
     * Method is used to get correct currency pair from database.
     *
     * @param currency1symbol first currency symbol as {@link String}.
     * @param currency2symbol second currency symbol as {@link String}.
     * @return correct {@link CurrencyPairEntity} if pair is found. Null otherwise.
     */
    public CurrencyPairEntity getCurrencyPair(String currency1symbol, String currency2symbol) {

        //try to find corresponding currencies in database
        CurrencyEntity currency1 = getCurrencyBySymbol(currency1symbol);
        CurrencyEntity currency2 = getCurrencyBySymbol(currency2symbol);

        //check if currencies successfully found in database
        if (currency1 == null) {
            logger.warn("Currency symbol '{}' not found in database", currency1symbol);
            return null;
        } else if (currency2 == null) {
            logger.warn("Currency symbol '{}' not found in database", currency2symbol);
            return null;
        }

        //get all pairs with currency1
        List<CurrencyPairEntity> pairsWithCur1 = Stream.concat(
                        Optional.ofNullable(currencyPairRepository.findByCurrency1(currency1))
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .filter(Objects::nonNull),
                        Optional.ofNullable(currencyPairRepository.findByCurrency2(currency1))
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .filter(Objects::nonNull)
                )
                .collect(Collectors.toList());

        //check found pairs if they contain currency2
        for (CurrencyPairEntity currencyPair : pairsWithCur1) {
            if (currencyPair.getCurrency1().getCurrencyNameUser().equals(currency2.getCurrencyNameUser()) ||
                    currencyPair.getCurrency2().getCurrencyNameUser().equals(currency2.getCurrencyNameUser()))
                return currencyPair;
        }
        logger.warn("Currency pair {}/{} is not found in database.", currency1symbol, currency2symbol);
        return null;
    }

    public CurrencyPairEntity getCurrencyPair(List<String> currencies) {
        //todo this is temprorary
        if (currencies.size() != 2) throw new RuntimeException("Error!");
        return this.getCurrencyPair(currencies.get(0), currencies.get(1));
    }

    /**
     * Get user-friendly available currencies.
     *
     * @return all available currencies as {@link String}.
     */
    public String getAllCurrenciesList() {

        Set<CurrencyEntity> allCurrenciesList = this.getAllCurrencies();

        return allCurrenciesList.stream()
                .map(a -> a.getCurrencyFullName() + ": " + a.getCurrencyNameUser())
                .collect(Collectors.joining("\n"));
    }

}
