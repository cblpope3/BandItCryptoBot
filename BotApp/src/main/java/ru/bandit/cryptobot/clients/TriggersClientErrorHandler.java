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
public class TriggersClientErrorHandler implements ResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(TriggersClientErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws ResponseStatusException, IOException {

        if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {

            logger.warn("Got response from Trigger-App: trigger not found.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        } else if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {

            logger.error("Got response from Trigger-App: bad request.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        } else {

            //unreachable statement
            logger.error("Unknown error code: {}", httpResponse.getStatusCode());
            throw new ResponseStatusException(httpResponse.getStatusCode());

        }
    }
}
