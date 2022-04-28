package ru.bandit.cryptobot.bot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class implementing help bot menu item.
 *
 * @see AbstractMenuItem
 */
@Component
@SuppressWarnings("unused")
public class MenuHelp extends AbstractMenuItem {

    @Autowired
    protected MenuHelp(Menu01Main parent) {
        super(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String registerCommand() {
        return "help";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return "- Все команды должны начинаться со знака \"/\". Например /start, /help, /ONCE/BTC/USD\n" +
                "- /start - начать работу с ботом\n" +
                "- /help - показать список возможных команд\n" +
                "- /stop -  удалить все созданные подписки пользователя\n" +
                "- /resume - восстановить оповещения по всем созданным подпискам\n" +
                "- /pause - прекратить оповещения по всем созданным подпискам\n" +
                "- /my_subscriptions - показать все созданные подписки. Рекомендация: пользуйтесь командой в режиме " +
                "/pause для удобства использования.\n" +
                "- /unsubscribe/sub_id - удалить подписку с идентификационным номером sub_id. " +
                "Рекомендация: пользуйтесь командой в режиме /pause для удобства использования.\n" +
                "- /target_up/cur1/cur2/val - создать уведомление о превышении курса выше целевого значения " +
                "на заданный процент, где cur1 и cur2 - коды доступных валют, val - значение изменения в %. " +
                "Например: /target_up/btc/rub/up/10.\n" +
                "- /target_down/cur1/cur2/val - создать уведомление о снижении курса ниже целевого значения " +
                "на заданный процент, где cur1 и cur2 - коды доступных валют, val - значение изменения в %. " +
                "Например: /target_down/btc/rub/up/10.\n" +
                "- /average/cur1/cur2 - создать подписку на скользящее среднее значение за 1 минуту.\n" +
                "- /simple/cur1/cur2 - создать подписку на курс валют, где cur1 и cur2 - коды доступных валют.\n" +
                "- /all_cur - отобразить доступные валюты. " +
                "Рекомендация: пользуйтесь командой в режиме /pause для удобства использования.\n" +
                "- /once/cur1/cur2 -  однократно узнать стоимость пары валют, где cur1 и cur2 - коды доступных валют. " +
                "Например /ONCE/BTC/USD. Соответствует кнопке \"Текущий курс\".\n" +
                "- Рекомендуем пользоваться интерактивным режимом работы с роботом, последовательно нажимая кнопки на экране.";
    }
}