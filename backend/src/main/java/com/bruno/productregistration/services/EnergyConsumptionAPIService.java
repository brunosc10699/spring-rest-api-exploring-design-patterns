package com.bruno.productregistration.services;

import com.bruno.productregistration.entities.EnergyConsumption;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appliances", url = "https://happliance.herokuapp.com/api/v1/appliances")
public interface EnergyConsumptionAPIService {

    @GetMapping(value = "/name")
    EnergyConsumption getConsumption(@RequestParam("name") String name);
}
