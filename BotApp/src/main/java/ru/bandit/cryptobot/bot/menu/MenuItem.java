package ru.bandit.cryptobot.bot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.List;

public interface MenuItem {

    String getText(UserDTO user, List<String> param);

    InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param);

}
