package ru.bandit.cryptobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.bandit.cryptobot.service.ApiService;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class ApiManager {

    private static final long COOL_DOWN_DELAY = 60000L;
    private final ApiService mainApi;
    private final ApiService reserveApi;
    Logger logger = LoggerFactory.getLogger(ApiManager.class);
    private ApiService currentApi;

    @Autowired
    public ApiManager(@Qualifier("Binance") ApiService mainApi, @Qualifier("CoinGecko") ApiService reserveApi) {
        this.mainApi = mainApi;
        this.reserveApi = reserveApi;
        currentApi = mainApi;
    }

    public ApiService getCurrentApi() {
        return currentApi;
    }

    public void switchToReserveApi() {
        logger.warn("switching to reserve api...");
        currentApi = reserveApi;
        startCoolDown();
    }

    public void switchToMainApi() {
        logger.warn("switching to main api...");
        currentApi = mainApi;
    }

    public void switchToOtherApi() {
        if (this.currentApi == mainApi) this.currentApi = reserveApi;
        else this.currentApi = mainApi;
    }

    private void startCoolDown() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                logger.debug("Cooldown timer is ran out. Switching to main api.");
                switchToMainApi();
            }
        };

        Timer timer = new Timer("Timer");

        timer.schedule(timerTask, COOL_DOWN_DELAY);
    }
}
