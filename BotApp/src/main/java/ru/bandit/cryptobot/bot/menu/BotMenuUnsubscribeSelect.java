package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.entities.UserTriggerEntity;
import ru.bandit.cryptobot.repositories.UserTriggersRepository;
import ru.bandit.cryptobot.services.UsersService;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMenuUnsubscribeSelect implements MenuItem {

    @Autowired
    UserTriggersRepository userTriggersRepository;

    @Autowired
    UsersService usersService;

    @Override
    public String getText(Long userId, List<String> param) {
        return "Выберите подписку для удаления:";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(Long userId, List<String> param) {

        List<UserTriggerEntity> subscriptionsList = userTriggersRepository.findByUser(usersService.getUser(userId));

        List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();

        for (UserTriggerEntity subscription : subscriptionsList) {

            //FIXME button name is not fine for simple triggers
            //№ - XXX/YYY/Тип триггера/Период/Искомое значение
            InlineKeyboardButton button = new InlineKeyboardButton(
                    String.format("№%d - %s/%s - %s - %d",
                            subscription.getId(),
                            subscription.getCurrencyPair().getCurrency1().getCurrencyNameUser(),
                            subscription.getCurrencyPair().getCurrency2().getCurrencyNameUser(),
                            subscription.getTriggerType().getTriggerName(),
                            subscription.getTargetValue()));

            button.setCallbackData(MenuItemsEnum.UNSUBSCRIBE.toString() + '/' + subscription.getId().toString());

            List<InlineKeyboardButton> row = List.of(button);

            buttonsGrid.add(row);
        }

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.OPERATIONS.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        buttonsGrid.add(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}