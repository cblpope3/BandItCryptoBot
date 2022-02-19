package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.ChatEntity;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.List;

@Component
public class BotMenuShowAll implements MenuItem {

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    @Override
    public String getText(Long userId, List<String> param) {

        ChatEntity chat = activeChatsRepository.findByChatName(userId);

        List<MailingListEntity> subscriptionsList = mailingListRepository.findByChat(chat);

        if (subscriptionsList.isEmpty()) return "У вас нет подписок";
        //FIXME output is not fine
        else return subscriptionsList.toString();
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.OPERATIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}