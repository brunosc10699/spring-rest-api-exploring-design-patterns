package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.services.EnergyConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/consumptions")
@RequiredArgsConstructor
public class EnergyConsumptionResource {

    private final EnergyConsumptionService energyConsumptionService;

    @PostMapping
    public ResponseEntity<EnergyConsumptionDTO> save(@Valid @RequestBody EnergyConsumptionDTO consumptionDTO){
        consumptionDTO = energyConsumptionService.save(consumptionDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(consumptionDTO.getName())
                .toUri();
        return ResponseEntity.created(uri).body(consumptionDTO);
    }


}
