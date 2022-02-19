package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.CurrencyEntity;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyEntity, Integer> {
    CurrencyEntity findByCurrencyNameUser(String currencyNameUser);

    List<CurrencyEntity> findAll();
}
