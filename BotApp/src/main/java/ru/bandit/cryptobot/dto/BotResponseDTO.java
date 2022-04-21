package ru.bandit.cryptobot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

//todo write javadoc
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotResponseDTO {
    private InlineKeyboardMarkup keyboard;
    private String message;
}
