package ru.bandit.cryptobot.data_containers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trigger {

    int id;
    String currency;
    int type;
}
