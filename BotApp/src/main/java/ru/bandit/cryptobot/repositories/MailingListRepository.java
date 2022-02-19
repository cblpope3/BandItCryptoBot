package ru.bandit.cryptobot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.entities.MailingListEntity;

import java.util.List;

@Repository
public interface MailingListRepository extends CrudRepository<MailingListEntity, Integer> {
    List<MailingListEntity> findByCurrency(String currency);

    MailingListEntity findByChatAndCurrency(ChatEntity chat, String currency);

    List<MailingListEntity> findByChat(ChatEntity chat);
}