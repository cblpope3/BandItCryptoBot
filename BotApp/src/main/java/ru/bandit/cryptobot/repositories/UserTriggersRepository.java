package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.CurrencyPairEntity;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;
import ru.bandit.cryptobot.entities.UserEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;

import java.util.List;

@Repository
public interface UserTriggersRepository extends CrudRepository<UserTriggerEntity, Integer> {
    UserTriggerEntity findById(Long id);

    List<UserTriggerEntity> findByUser(UserEntity user);

    List<UserTriggerEntity> findByUserAndTriggerTypeAndCurrencyPairAndTargetValue(UserEntity user,
                                                                                  TriggerTypeEntity triggerType,
                                                                                  CurrencyPairEntity currencyPair,
                                                                                  Double targetValue);

    List<UserTriggerEntity> findByTriggerType(TriggerTypeEntity triggerType);

    List<UserTriggerEntity> findByTriggerTypeAndUser_IsPaused(TriggerTypeEntity triggerType, boolean isPaused);
}
