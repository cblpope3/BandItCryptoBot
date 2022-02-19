package ru.bandit.cryptobot.bot.menu;

public enum MenuItemsEnum {

    START,
    MAIN,
        ALL_CUR,
        OPERATIONS,
            SELECT_1_CUR,
                SELECT_2_CUR,
                    TRIGGER_TYPE,
                        ONCE,
                        SIMPLE,
                        PERIOD,
                            AVERAGE,
                        DIRECTION,
                            VALUE,
                                TARGET,
            UNSUBSCRIBE_SELECT,
                UNSUBSCRIBE,
            SHOW_ALL,
        PAUSE,
        RESUME,
        STOP_CONFIRM,
            STOP,
        HELP
}
