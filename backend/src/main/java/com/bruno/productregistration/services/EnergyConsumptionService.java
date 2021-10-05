package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnergyConsumptionService {

    EnergyConsumption save(EnergyConsumption consumption);

    Page<EnergyConsumptionDTO> findAll(Pageable pageable);

    EnergyConsumption findByNameIgnoreCase(EnergyConsumption consumption);

    EnergyConsumptionDTO findById(String id);

    EnergyConsumptionDTO update(String name, EnergyConsumptionDTO energyConsumptionDTO);

    void delete(String name);
}
