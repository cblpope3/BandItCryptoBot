package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.repositories.CurrencyRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * This service implements interaction with currencies database table.
 */
@Service
public class CurrencyService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Get currency from database by its name.
     *
     * @param symbol currency name (for example "RUB", or "BTC").
     * @return found currency as {@link CurrencyEntity}. If requested currency not found in database return null.
     */
    public CurrencyEntity getCurrencyBySymbol(String symbol) {
        CurrencyEntity foundCurrency = currencyRepository.findByCurrencyNameUser(symbol);
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
}
