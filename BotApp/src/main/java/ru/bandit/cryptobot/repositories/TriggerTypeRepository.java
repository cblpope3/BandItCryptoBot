package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.TriggerTypeEntity;

@Repository
public interface TriggerTypeRepository extends CrudRepository<TriggerTypeEntity, Integer> {
    TriggerTypeEntity findByTriggerName(String triggerName);
}
