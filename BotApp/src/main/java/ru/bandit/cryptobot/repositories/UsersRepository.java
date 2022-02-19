package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.UserEntity;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByChatId(Long chatId);
}
