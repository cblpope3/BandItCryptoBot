package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.UserEntity;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Integer> {
    /**
     * @deprecated use {@link #findByUserId(Long)} instead
     */
    @Deprecated(forRemoval = true)
    UserEntity findByChatId(Long chatId);

    UserEntity findByUserId(Long userId);
}
