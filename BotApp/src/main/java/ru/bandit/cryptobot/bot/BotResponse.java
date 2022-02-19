package ru.bandit.cryptobot.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotResponse {
    private InlineKeyboardMarkup keyboard;
    private String message;
}
