package ru.bandit.cryptobot.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class BinanceApiErrorHandler implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(BinanceApiErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws ResponseStatusException, IOException {

        if (httpResponse.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {

            //handle error status codes
            logger.error("Too much requests to binance.com");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);

        } else if (httpResponse.getStatusCode() == HttpStatus.I_AM_A_TEAPOT) {

            //handle ban
            logger.error("Banned in binance.com api.");
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);

        } else {

            //unreachable statement
            logger.error("Unknown error code: {}", httpResponse.getStatusCode());
            throw new ResponseStatusException(httpResponse.getStatusCode());

        }
    }
}
