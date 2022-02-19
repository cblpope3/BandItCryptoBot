package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.ChatEntity;

@Repository
public interface ActiveChatsRepository extends CrudRepository<ChatEntity, Integer> {
    ChatEntity findById(int id);
}