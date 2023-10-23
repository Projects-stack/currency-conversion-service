package com.in28minutes.microservices.currencyconversionservice.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import com.in28minutes.microservices.currencyconversionservice.business.common.GlobalConstants;
import com.in28minutes.microservices.currencyconversionservice.client.CurrencyExchangeClient;
import com.in28minutes.microservices.currencyconversionservice.db.entity.CurrencyConversionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(GlobalConstants.API + "/currency-conversion")
public class CurrencyConversionController
{

    @Autowired
    private CurrencyExchangeClient currencyExchangeClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionEntity calculateCurrencyConversion(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            @PathVariable("quantity") BigDecimal quantity
    )
    {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversionEntity> responseEntity = restTemplate.getForEntity("http://localhost:8000/api/currency-exchange/from/{from}/to/{to}", CurrencyConversionEntity.class,
                uriVariables);
        CurrencyConversionEntity currencyConversionEntity = responseEntity.getBody();
        assert currencyConversionEntity != null;
        return new CurrencyConversionEntity(currencyConversionEntity.getId(), from, to, quantity, currencyConversionEntity.getConversionMultiple(), quantity.multiply(currencyConversionEntity.getConversionMultiple()), currencyConversionEntity.getEnvironment());
    }

    @GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionEntity calculateCurrencyConversionFeign(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            @PathVariable("quantity") BigDecimal quantity
    )
    {
        CurrencyConversionEntity currencyConversionEntity = currencyExchangeClient.retrieveExchangeValue(from, to);
        return new CurrencyConversionEntity(currencyConversionEntity.getId(), from, to, quantity, currencyConversionEntity.getConversionMultiple(), quantity.multiply(currencyConversionEntity.getConversionMultiple()), currencyConversionEntity.getEnvironment());
    }
}
