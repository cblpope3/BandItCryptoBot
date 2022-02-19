package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.CurrencyEntity;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;

@Repository
public interface CurrencyPairRepository extends CrudRepository<CurrencyPairEntity, Integer> {
    CurrencyPairEntity findByCurrency1AndCurrency2(CurrencyEntity currency1, CurrencyEntity currency2);
}
