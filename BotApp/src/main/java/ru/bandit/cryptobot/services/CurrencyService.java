package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dao.AverageCurrencyRatesDAO;
import ru.bandit.cryptobot.dao.CurrentCurrencyRatesDAO;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.CurrencyException;
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

    CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    AverageCurrencyRatesDAO averageCurrencyRatesDAO;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyPairRepository currencyPairRepository,
                           CurrentCurrencyRatesDAO currentCurrencyRatesDAO,
                           AverageCurrencyRatesDAO averageCurrencyRatesDAO) {
        this.currencyRepository = currencyRepository;
        this.currencyPairRepository = currencyPairRepository;
        this.currentCurrencyRatesDAO = currentCurrencyRatesDAO;
        this.averageCurrencyRatesDAO = averageCurrencyRatesDAO;
    }

    /**
     * Get currency from database by its name.
     *
     * @param symbol currency name (for example "RUB", or "BTC").
     * @return found currency as {@link CurrencyEntity}.
     * @throws CommonBotAppException if currency not found.
     */
    public CurrencyEntity getCurrencyBySymbol(String symbol) throws CommonBotAppException {
        CurrencyEntity foundCurrency = currencyRepository.findByCurrencyNameUser(symbol.toUpperCase());
        if (foundCurrency == null) {
            logger.warn("Requested currency {} not found!", symbol);
            throw new CurrencyException("Currency not found.", CurrencyException.ExceptionCause.NO_CURRENCY);
        }
        return foundCurrency;
    }

    /**
     * Get all currencies stored in database.
     *
     * @return {@link Set} of {@link CurrencyEntity}.
     * @throws CommonBotAppException if not found any currency in database.
     * @see #getAllCurrenciesList()
     */
    public Set<CurrencyEntity> getAllCurrencies() throws CommonBotAppException {
        Set<CurrencyEntity> resultSet = new HashSet<>(currencyRepository.findAll());
        if (resultSet.isEmpty()) {
            logger.warn("Not found any currency in database!");
            throw new CurrencyException("Not found any currency in database.", CurrencyException.ExceptionCause.NO_CURRENCIES_FOUND);
        }
        return resultSet;
    }

    /**
     * Method is used to get correct currency pair from database.
     *
     * @param currency1symbol first currency symbol as {@link String}.
     * @param currency2symbol second currency symbol as {@link String}.
     * @return correct {@link CurrencyPairEntity} if pair is found.
     * @throws CommonBotAppException if any of requested currencies not found, currency pair not found, or requested currency
     *                               pair of two same currencies.
     * @see #getCurrencyBySymbol(String)
     * @see #getCurrencyPair(List)
     */
    public CurrencyPairEntity getCurrencyPair(String currency1symbol, String currency2symbol) throws CommonBotAppException {

        //try to find corresponding currencies in database
        CurrencyEntity currency1 = this.getCurrencyBySymbol(currency1symbol);
        CurrencyEntity currency2 = this.getCurrencyBySymbol(currency2symbol);

        //check if currencies successfully found in database
        if (currency1.equals(currency2)) {
            logger.warn("Requested currency pair of two same currencies '{}'", currency1symbol);
            throw new CurrencyException("Requested pair of same currencies.", CurrencyException.ExceptionCause.SAME_CURRENCIES_IN_REQUEST);
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
        throw new CurrencyException("Currency pair not found.", CurrencyException.ExceptionCause.NO_CURRENCY_PAIR);
    }

    /**
     * Method is used to get correct currency pair from database.
     *
     * @param currencies currencies symbols as {@link List} of {@link String}.
     * @return correct {@link CurrencyPairEntity} if pair is found.
     * @throws CommonBotAppException if any of requested currencies not found, currency pair not found, requested currency
     *                               pair of two same currencies, or input {@link List} has wrong size (correct size is = 2).
     * @see #getCurrencyPair(String, String)
     */
    public CurrencyPairEntity getCurrencyPair(List<String> currencies) throws CommonBotAppException {
        //todo this is temporary (don't remember why)
        if (currencies.size() != 2) {
            logger.error("Request has wrong size: {}.", currencies.size());
            throw new CurrencyException("Wrong request List size.", CurrencyException.ExceptionCause.WRONG_PARAMETERS);
        }
        return this.getCurrencyPair(currencies.get(0), currencies.get(1));
    }

    /**
     * Get user-friendly available currencies.
     *
     * @return all available currencies as {@link String}.
     * @throws CommonBotAppException if no available currencies found.
     * @see #getAllCurrencies()
     */
    public String getAllCurrenciesList() throws CommonBotAppException {

        Set<CurrencyEntity> allCurrenciesList = this.getAllCurrencies();

        return allCurrenciesList.stream()
                .map(a -> a.getCurrencyFullName() + ": " + a.getCurrencyNameUser())
                .collect(Collectors.joining("\n"));
    }

    /**
     * Get current currency rate.
     *
     * @param currencyPair requested currency pair.
     * @return currency rate value.
     * @see CurrencyPairEntity
     * @see #getAverageCurrencyRate(CurrencyPairEntity)
     */
    public Double getCurrentCurrencyRate(CurrencyPairEntity currencyPair) {
        return currentCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameSource() +
                currencyPair.getCurrency2().getCurrencyNameSource());
    }

    /**
     * Get average currency rate.
     *
     * @param currencyPair requested currency pair.
     * @return currency rate value.
     * @see CurrencyPairEntity
     * @see #getCurrentCurrencyRate(CurrencyPairEntity)
     */
    public Double getAverageCurrencyRate(CurrencyPairEntity currencyPair) {
        return averageCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameSource() +
                currencyPair.getCurrency2().getCurrencyNameSource());
    }

}
