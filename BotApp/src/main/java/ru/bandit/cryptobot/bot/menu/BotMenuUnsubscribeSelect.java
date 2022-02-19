package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.MailingListEntity;
import ru.bandit.cryptobot.repositories.ActiveChatsRepository;
import ru.bandit.cryptobot.repositories.MailingListRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMenuUnsubscribeSelect implements MenuItem {

    @Autowired
    MailingListRepository mailingListRepository;

    @Autowired
    ActiveChatsRepository activeChatsRepository;

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите подписку для удаления:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        List<MailingListEntity> subscriptionsList = mailingListRepository.findByChat(activeChatsRepository.findByChatName(userId));

        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        for (MailingListEntity subscription : subscriptionsList) {

            //FIXME button name is not fine
            InlineKeyboardButton button = new InlineKeyboardButton(subscription.getCurrency());
            button.setCallbackData(MenuItemsEnum.UNSUBSCRIBE.toString() + '/' + subscription.getCurrency());

            List<InlineKeyboardButton> row = List.of(button);

            buttonsGrid.add(row);
        }

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.SUBSCRIPTIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        buttonsGrid.add(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}