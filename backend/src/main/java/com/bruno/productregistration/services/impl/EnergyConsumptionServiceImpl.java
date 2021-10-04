package com.bruno.productregistration.services.impl;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.repositories.EnergyConsumptionRepository;
import com.bruno.productregistration.services.EnergyConsumptionAPIService;
import com.bruno.productregistration.services.EnergyConsumptionService;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    private final EnergyConsumptionRepository energyConsumptionRepository;

    private final EnergyConsumptionAPIService energyConsumptionAPIService;

    @Override
    public EnergyConsumption save(EnergyConsumption consumption) {
        consumption = checkEnergyConsumptionObject(consumption);
        try {
            return energyConsumptionRepository.save(consumption);
        } catch (NullPointerException e) {
            log.info("The EnergyConsumption object was null!");
        }
        return consumption;
    }

    @Transactional(readOnly = true)
    @Override
    public EnergyConsumption findByNameIgnoreCase(EnergyConsumption consumption) {
        return energyConsumptionRepository.findByNameIgnoreCase(consumption.getName())
                .orElseGet(() -> {
                    try {
                        EnergyConsumption newConsumption = energyConsumptionAPIService.getConsumption(consumption.getName());
                        return newConsumption;
                    } catch (FeignException e) {
                        log.info("Energy consumption information about " + consumption.getName() + " was not found!");
                    } catch (NullPointerException e) {
                        log.info(e.getLocalizedMessage());
                    }
                    return consumption;
                });
    }

    @Override
    public EnergyConsumptionDTO findById(String id) {
        EnergyConsumption consumption = energyConsumptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return EnergyConsumptionDTO.toDTO(consumption);
    }

    private EnergyConsumption checkEnergyConsumptionObject(EnergyConsumption energyConsumption) {
        try {
            String name = energyConsumption.getName();
            if (name.length() > 2) {
                Integer power = energyConsumption.getPower();
                EnergyConsumption consumption = findByNameIgnoreCase(energyConsumption);
                return consumption;
            }
        } catch (NullPointerException e) {
            log.info("One of the energy consumption object attribute was null!");
        }
        return energyConsumption;
    }

}
