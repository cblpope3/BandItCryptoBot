package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.MetricsEntity;

@Repository
public interface MetricsRepository extends CrudRepository<MetricsEntity, Integer> {
    MetricsEntity findById(Long id);
}
