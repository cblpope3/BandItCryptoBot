package ru.bandit.cryptobot.bot.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface MenuItem {

    String getText(Long userId, List<String> param);

    InlineKeyboardMarkup getMarkup(Long userId, List<String> param);

}
