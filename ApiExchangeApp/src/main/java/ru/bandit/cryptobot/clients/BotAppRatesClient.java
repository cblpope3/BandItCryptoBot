package ru.bandit.cryptobot.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.dto.CurrencyRatesDTO;

import java.util.List;

public abstract class BotAppRatesClient {

    protected final RestTemplate restTemplate;

    @Value("${bot-app.hostname}")
    protected String botAppCurrencyUrl;

    protected BotAppRatesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public abstract void postNewRates(List<CurrencyRatesDTO> newRates);
}
