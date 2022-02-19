package ru.bandit.cryptobot.timers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.MainThread;

@Component
public class MainThreadTimer {

    @Autowired
    MainThread mainThread;

    @Scheduled(fixedDelay = 5000)
    public void fetchNewData() {
        mainThread.performDataCycle();
    }
}
