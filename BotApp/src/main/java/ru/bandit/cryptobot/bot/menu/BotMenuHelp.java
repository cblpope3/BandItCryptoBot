package ru.bandit.cryptobot.bot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bandit.cryptobot.dto.UserDTO;

import java.util.List;

@Component
public class BotMenuHelp implements MenuItem {

    @Override
    public String getText(UserDTO user, List<String> param) {
        return "- Все команды должны начинаться со знака \"/\". Например /start, /help, /ONCE/BTC/USD\n" +
                "- /start - начать работу с ботом\n" +
                "- /help - показать список возможных команд\n" +
                "- /stop -  удалить все созданные подписки пользователя\n" +
                "- /resume - восстановить оповещения по всем созданным подпискам\n" +
                "- /pause - прекратить оповещения по всем созданным подпискам\n" +
                "- /subscriptions/showall - показать все созданные подписки. Рекомендация: пользуйтесь командой в режиме /pause для удобства использования.\n" +
                "- /unsubscribe/sub_id - удалить подписку с идентификационным номером sub_id. Рекомендация: пользуйтесь командой в режиме /pause для удобства использования.\n" +
                "- /target_up/cur1/cur2/val - создать подписку на достижение целевого изменения (повышение или понижение) от текущего курса на заданный процент, где cur1 и cur2 - коды доступных валют, dir - целевое направление изменения UP || DOWN, val - значение изменения в %. Пример /target/btc/rub/up/10. Соответствует кнопке \"Будильник\".\n" +
                "- /average/cur1/cur2 - создать подписку на скользящее среднее значение за 1 минуту. Соответствует кнопке \"Среднее за минуту\".\n" +
                "- /simple/cur1/cur2 - создать подписку на курс валют, где cur1 и cur2 - коды доступных валют. Соответствует кнопке \"Рассылка\".\n" +
                "- /all_cur - отобразить доступные валюты. Рекомендация: пользуйтесь командой в режиме /pause для удобства использования.\n" +
                "- /once/cur1/cur2 -  однократно узнать стоимость пары валют, где cur1 и cur2 - коды доступных валют. Например /ONCE/BTC/USD. Соответствует кнопке \"Текущий курс\".\n" +
                "- Рекомендуем пользоваться интерактивным режимом работы с роботом, последовательно нажимая кнопки на экране.";
    }

    @Override
    public InlineKeyboardMarkup getMarkup(UserDTO user, List<String> param) {
        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData(MenuItemsEnum.MAIN.toString());

        List<InlineKeyboardButton> lastRow = List.of(buttonBack);

        List<List<InlineKeyboardButton>> buttonsGrid = List.of(lastRow);

        return new InlineKeyboardMarkup(buttonsGrid);
    }
}