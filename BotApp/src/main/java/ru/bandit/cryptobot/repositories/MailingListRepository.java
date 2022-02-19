package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.MailingListEntity;

@Repository
public interface MailingListRepository extends CrudRepository<MailingListEntity, Integer> {
}
