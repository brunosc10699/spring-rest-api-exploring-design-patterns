package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnergyConsumptionService {

    EnergyConsumptionDTO save(EnergyConsumptionDTO consumptionDTO);

    Page<EnergyConsumptionDTO> findAll(Pageable pageable);

    EnergyConsumptionDTO findByNameIgnoreCase(EnergyConsumptionDTO consumptionDTO);

    EnergyConsumptionDTO findById(String id);

    EnergyConsumptionDTO update(String name, EnergyConsumptionDTO energyConsumptionDTO);

    void delete(String name);
}
