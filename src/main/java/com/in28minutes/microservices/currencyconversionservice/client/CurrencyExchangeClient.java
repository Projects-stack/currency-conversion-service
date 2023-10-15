package com.in28minutes.microservices.currencyconversionservice.client;

import com.in28minutes.microservices.currencyconversionservice.business.common.GlobalConstants;
import com.in28minutes.microservices.currencyconversionservice.db.entity.CurrencyConversionEntity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeClient
{
    @GetMapping(value = GlobalConstants.API + "/currency-exchange/from/{from}/to/{to}")
    CurrencyConversionEntity retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
}
