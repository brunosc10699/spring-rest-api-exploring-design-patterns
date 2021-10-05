package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "appliances", url = "https://happliance.herokuapp.com/api/v1/appliances")
@FeignClient(name = "appliances", url = "http://localhost:8080/api/v1/appliances")
public interface EnergyConsumptionAPIService {

    @GetMapping(value = "/name")
    EnergyConsumptionDTO getConsumption(@RequestParam("name") String name);
}
