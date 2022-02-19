package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.UserEntity;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;
import ru.bandit.cryptobot.repositories.UsersRepository;

import java.util.List;

@Component
public class BotMenuShowAll implements MenuItem {

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public String getText(Long userId, List<String> param) {

        UserEntity user = usersRepository.findByChatId(userId);

        List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(user);

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