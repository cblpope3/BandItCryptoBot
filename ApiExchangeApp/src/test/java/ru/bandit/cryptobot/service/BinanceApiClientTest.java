package ru.bandit.cryptobot.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import ru.bandit.cryptobot.clients.BinanceApiClient;

@RunWith(MockitoJUnitRunner.class)
public class BinanceApiClientTest extends TestCase {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BinanceApiClient testingService;

    @Test
    public void testGetAllCurrencyPricesFineResponse() {

//        BinanceResponse testResponse1 = new BinanceResponse("BTCUSD", "89.99");
//        BinanceResponse testResponse2 = new BinanceResponse("BTCRUB", "8999.99");
//
//        List<BinanceResponse> receivedList = List.of(testResponse1, testResponse2);
//
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity<List<BinanceResponse>> goodResponse = new ResponseEntity<>(receivedList,
//                header,
//                HttpStatus.OK);
//
//        Mockito.when(restTemplate.exchange(
//                        ArgumentMatchers.anyString(),
//                        ArgumentMatchers.any(HttpMethod.class),
//                        ArgumentMatchers.isNull(),
//                        ArgumentMatchers.<ParameterizedTypeReference<List<BinanceResponse>>>any()))
//                .thenReturn(goodResponse);
//
//
//        assertEquals("{BTCRUB=8999.99, BTCUSD=89.99}", testingService.getAllCurrencyPrices().toString());
    }

    @Test
    public void testGetAllCurrencyPricesBadResponse() {

//        boolean didExceptionHappen = false;
//
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(MediaType.APPLICATION_JSON);
//
//        ResponseEntity<List<BinanceResponse>> badResponse = new ResponseEntity<>(null,
//                header,
//                HttpStatus.TOO_MANY_REQUESTS);
//
//        Mockito.when(restTemplate.exchange(
//                        ArgumentMatchers.anyString(),
//                        ArgumentMatchers.any(HttpMethod.class),
//                        ArgumentMatchers.isNull(),
//                        ArgumentMatchers.<ParameterizedTypeReference<List<BinanceResponse>>>any()))
//                .thenReturn(badResponse);
//
//        try {
//            testingService.getAllCurrencyPrices();
//        } catch (ResponseStatusException e) {
//            didExceptionHappen = true;
//        }
//
//        assertTrue(didExceptionHappen);
    }
}