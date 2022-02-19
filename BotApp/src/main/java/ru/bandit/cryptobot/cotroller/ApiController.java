package ru.bandit.cryptobot.cotroller;

import org.springframework.web.bind.annotation.*;
import ru.bandit.cryptobot.entities.RatesJSONContainer;

@RestController
@RequestMapping("/rates")
public class ApiController {

    @PostMapping("")
    public String gotNewRates(@RequestBody RatesJSONContainer newRates){
        //TODO this is dummy method
        System.out.println("got new rates: "+ newRates.toString());
        return "Got new data: "+newRates.toString();
    }
}
