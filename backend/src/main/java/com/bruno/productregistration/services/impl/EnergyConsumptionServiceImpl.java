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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    private final EnergyConsumptionRepository energyConsumptionRepository;

    private final EnergyConsumptionAPIService energyConsumptionAPIService;

    @Override
    public EnergyConsumptionDTO save(EnergyConsumptionDTO consumptionDTO) {
        consumptionDTO = checkEnergyConsumptionObject(consumptionDTO);
        try {
            if (consumptionDTO != null) {
                EnergyConsumption consumption = energyConsumptionRepository.save(fromDTO(consumptionDTO));
                return EnergyConsumptionDTO.toDTO(consumption);
            }
        } catch (NullPointerException e) {
            log.info("The EnergyConsumption object was null!");
        }
        return consumptionDTO;
    }

    @Override
    public Page<EnergyConsumptionDTO> findAll(Pageable pageable) {
        return energyConsumptionRepository.findAll(pageable).map(EnergyConsumptionDTO::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public EnergyConsumptionDTO findByNameIgnoreCase(EnergyConsumptionDTO consumptionDTO) {
        return energyConsumptionRepository.findByNameIgnoreCase(consumptionDTO.getName())
                .orElseGet(() -> {
                    try {
                        EnergyConsumptionDTO newConsumption = energyConsumptionAPIService.getConsumption(consumptionDTO.getName());
                        return newConsumption;
                    } catch (FeignException e) {
                        log.info("Energy consumptionDTO information about " + consumptionDTO.getName() + " was not found!");
                    } catch (NullPointerException e) {
                        log.info(e.getLocalizedMessage());
                    }
                    return consumptionDTO;
                });
    }

    @Override
    public EnergyConsumptionDTO findById(String id) {
        EnergyConsumption consumption = energyConsumptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return EnergyConsumptionDTO.toDTO(consumption);
    }

    @Override
    public EnergyConsumptionDTO update(String name, EnergyConsumptionDTO consumptionDTO) {
        findById(name);
        consumptionDTO.setName(name);
        EnergyConsumption consumption = energyConsumptionRepository.save(fromDTO(consumptionDTO));
        return EnergyConsumptionDTO.toDTO(consumption);
    }

    @Override
    public void delete(String name) {
        findById(name);
        energyConsumptionRepository.deleteById(name);
    }

    private EnergyConsumptionDTO checkEnergyConsumptionObject(EnergyConsumptionDTO consumptionDTO) {
        try {
            if (consumptionDTO.getName().length() > 2) {
                Integer power = consumptionDTO.getPower();
                EnergyConsumptionDTO consumption = findByNameIgnoreCase(consumptionDTO);
                return consumption;
            }
        } catch (NullPointerException e) {
            log.info("One of the energy consumption object attribute was null!");
        }
        return EnergyConsumptionDTO.builder().build();
    }

    private EnergyConsumption fromDTO(EnergyConsumptionDTO consumptionDTO){
        return EnergyConsumption.builder()
                .name(consumptionDTO.getName())
                .power(consumptionDTO.getPower())
                .monthlyUsage(consumptionDTO.getMonthlyUsage())
                .dailyUse(consumptionDTO.getDailyUse())
                .monthlyConsumptionAverage(consumptionDTO.getMonthlyConsumptionAverage())
                .build();
    }

}
