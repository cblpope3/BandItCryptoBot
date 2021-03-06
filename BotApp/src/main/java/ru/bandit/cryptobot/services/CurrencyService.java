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

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This service implements interaction with currencies and currency pairs.
 */
@Service
public class CurrencyService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyPairRepository currencyPairRepository;

    private final CurrentCurrencyRatesDAO currentCurrencyRatesDAO;

    private final AverageCurrencyRatesDAO averageCurrencyRatesDAO;

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
    public CurrencyEntity getCurrencyBySymbol(@NotNull String symbol) throws CommonBotAppException {
        CurrencyEntity foundCurrency = currencyRepository.findByCurrencyNameUser(symbol.toUpperCase());
        if (foundCurrency == null) {
            if (logger.isDebugEnabled()) logger.debug("Requested currency {} not found!", symbol);
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
            logger.error("Not found any currency in database!");
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
            if (logger.isDebugEnabled())
                logger.debug("Requested currency pair of two same currencies '{}'", currency1symbol);
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
        if (logger.isDebugEnabled())
            logger.debug("Currency pair {}/{} is not found in database.", currency1symbol, currency2symbol);
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
        if (currencies.size() != 2) {
            logger.warn("Request has wrong size: {}.", currencies.size());
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

        if (logger.isTraceEnabled()) logger.trace("Generating list of all currencies...");
        Set<CurrencyEntity> allCurrenciesList = this.getAllCurrencies();

        if (logger.isDebugEnabled()) logger.debug("List of all available currencies is generated.");

        return allCurrenciesList.stream()
                .map(a -> a.getCurrencyFullName() + ": " + a.getCurrencyNameUser())
                .collect(Collectors.joining("\n"));
    }

    /**
     * Get current currency rate.
     *
     * @param currencyPair requested currency pair.
     * @return currency rate value.
     * @throws CurrencyException if no data about currency rates.
     * @see CurrencyPairEntity
     * @see #getAverageCurrencyRate(CurrencyPairEntity)
     */
    public Double getCurrentCurrencyRate(CurrencyPairEntity currencyPair) throws CurrencyException {
        if (logger.isTraceEnabled())
            logger.trace("Searching for {}/{} currency rate...", currencyPair.getCurrency1().getCurrencyNameUser(),
                    currencyPair.getCurrency2().getCurrencyNameUser());
        return currentCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameSource() +
                currencyPair.getCurrency2().getCurrencyNameSource());
    }

    /**
     * Get average currency rate.
     *
     * @param currencyPair requested currency pair.
     * @return currency rate value.
     * @throws CurrencyException if no data about average currency rates.
     * @see CurrencyPairEntity
     * @see #getCurrentCurrencyRate(CurrencyPairEntity)
     */
    public Double getAverageCurrencyRate(CurrencyPairEntity currencyPair) throws CurrencyException {
        if (logger.isTraceEnabled())
            logger.trace("Searching for {}/{} average currency rate...",
                    currencyPair.getCurrency1().getCurrencyNameUser(), currencyPair.getCurrency2().getCurrencyNameUser());
        return averageCurrencyRatesDAO.getRateBySymbol(currencyPair.getCurrency1().getCurrencyNameSource() +
                currencyPair.getCurrency2().getCurrencyNameSource());
    }

    /**
     * Get time when last currency rates update was received.
     *
     * @return last update time.
     */
    public String getLastRatesUpdateTime() {
        return this.currentCurrencyRatesDAO.getLastUpdateTime();
    }

    /**
     * Get time when last average currency rates update was received.
     *
     * @return last update time.
     */
    public String getLastAverageRatesUpdateTime() {
        return this.averageCurrencyRatesDAO.getLastUpdateTime();
    }

    /**
     * Get all available currencies {@link Set} that can make correct currency pair with given currency.
     *
     * @param currency given currency.
     * @return {@link Set} of complimentary currencies.
     * @throws CurrencyException if found no complimentary currencies.
     */
    public Set<CurrencyEntity> getAllComplimentaryCurrencies(CurrencyEntity currency) throws CurrencyException {
        if (logger.isTraceEnabled())
            logger.trace("Getting all complimentary currencies to {}", currency.getCurrencyNameUser());
        return this.getAllCurrencyPairsWithGiven(currency).stream()
                .map(currencyPair -> this.getOtherCurrencyFromPair(currencyPair, currency))
                .collect(Collectors.toSet());
    }

    /**
     * Method searches for all currency pairs with given currency.
     *
     * @param currency requested currency that must be contained in currency pairs.
     * @return {@link Set} of currency pairs that contain requested currency.
     * @throws CurrencyException if found no currency pair with given currency.
     */
    private Set<CurrencyPairEntity> getAllCurrencyPairsWithGiven(CurrencyEntity currency) throws CurrencyException {
        if (logger.isTraceEnabled()) logger.trace("Getting all currency pairs with {}", currency.getCurrencyNameUser());
        Set<CurrencyPairEntity> foundPairs = new HashSet<>();
        foundPairs.addAll(currencyPairRepository.findByCurrency1(currency));
        foundPairs.addAll(currencyPairRepository.findByCurrency2(currency));
        if (logger.isTraceEnabled())
            logger.trace("Currency pairs with {} are: {}", currency.getCurrencyNameUser(), foundPairs);
        if (foundPairs.isEmpty()) {
            throw new CurrencyException("Not found any pair with given currency.", CurrencyException.ExceptionCause.NOT_FOUND_PAIR_WITH_CURRENCY);
        }
        return foundPairs;
    }

    /**
     * Returns other currency from given currency pair.
     *
     * @param currencyPair currency pair that needed to processed.
     * @param currency     known currency.
     * @return other currency from currency pair.
     */
    private CurrencyEntity getOtherCurrencyFromPair(CurrencyPairEntity currencyPair, CurrencyEntity currency) {
        if (logger.isTraceEnabled())
            logger.trace("From currency pair {} trying to get currency other to {}", currencyPair, currency.getCurrencyNameUser());
        //assuming that currency pair consist of two different currencies.
        if (currencyPair.getCurrency1().equals(currency)) {
            if (logger.isTraceEnabled())
                logger.trace("Found currency {}.", currencyPair.getCurrency2().getCurrencyNameUser());
            return currencyPair.getCurrency2();
        } else if (currencyPair.getCurrency2().equals(currency)) {
            if (logger.isTraceEnabled())
                logger.trace("Found currency {}.", currencyPair.getCurrency1().getCurrencyNameUser());
            return currencyPair.getCurrency1();
        } else {
            //this statement must be unreachable. If we here, given currency not found in currency pair.
            IllegalArgumentException exception = new IllegalArgumentException(
                    "Problem with getting other currency from pair: given currency not found in currency pair.");
            logger.error(exception.getMessage());
            throw exception;
        }
    }

}
