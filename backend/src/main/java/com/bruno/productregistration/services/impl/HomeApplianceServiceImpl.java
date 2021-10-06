package com.bruno.productregistration.services.impl;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.entities.enums.Classification;
import com.bruno.productregistration.entities.enums.Voltage;
import com.bruno.productregistration.repositories.HomeApplianceRepository;
import com.bruno.productregistration.services.HomeApplianceService;
import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import com.bruno.productregistration.services.exceptions.IncorrectValueException;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class HomeApplianceServiceImpl implements HomeApplianceService {

    private final HomeApplianceRepository homeApplianceRepository;

    private final EnergyConsumptionServiceImpl energyConsumptionService;

    @Override
    public HomeApplianceDTO save(HomeApplianceDTO homeApplianceDTO) {
        checkEnums(homeApplianceDTO);
        checkApplianceNameValidity(homeApplianceDTO);
        setAttributeDefaultValue(homeApplianceDTO);
        saveEnergyConsumption(homeApplianceDTO);
        HomeAppliance product = fromHomeApplianceDTO(homeApplianceDTO);
        product = homeApplianceRepository.save(product);
        return HomeApplianceDTO.toDTO(product);
    }

    @Transactional(readOnly = true)
    @Override
    public HomeApplianceDTO findByNameIgnoreCase(String name) {
        HomeAppliance appliance = homeApplianceRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(name));
        return HomeApplianceDTO.toDTO(appliance);
    }

    @Transactional(readOnly = true)
    @Override
    public HomeApplianceDTO findById(String id) {
        HomeAppliance appliance = homeApplianceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));
        return HomeApplianceDTO.toDTO(appliance);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HomeApplianceDTO> findAll(Pageable pageable) {
        return homeApplianceRepository.findAll(pageable).map(HomeApplianceDTO::toDTO);
    }

    @Override
    public HomeApplianceDTO update(String id, HomeApplianceDTO homeApplianceDTO) {
        checkEnums(homeApplianceDTO);
        HomeApplianceDTO applianceRegistered = findById(id);
        homeApplianceDTO.setId(id);
        if (!homeApplianceDTO.getName().equals(applianceRegistered.getName()))
            checkApplianceNameValidity(homeApplianceDTO);
        checkEnergyConsumption(applianceRegistered, homeApplianceDTO);
        HomeAppliance appliance = fromHomeApplianceDTO(homeApplianceDTO);
        appliance = homeApplianceRepository.save(appliance);
        return HomeApplianceDTO.toDTO(appliance);
    }

    private void checkEnums(HomeApplianceDTO homeApplianceDTO) {
        try {
            Voltage.toEnum(homeApplianceDTO.getVoltage());
            Classification.toEnum(homeApplianceDTO.getClassification());
        } catch (IllegalArgumentException e) {
            throw new IncorrectValueException(e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        findById(id);
        homeApplianceRepository.deleteById(id);
    }

    private void checkEnergyConsumption(HomeApplianceDTO applianceRegistered, HomeApplianceDTO homeApplianceDTO) {
        try {
            if (!homeApplianceDTO.getEnergyConsumption().getName().equals(applianceRegistered.getEnergyConsumption().getName()))
                saveEnergyConsumption(homeApplianceDTO);
        } catch (NullPointerException e) {
            log.info("The energyConsumption's name attribute is null");
        }
    }

    private void saveEnergyConsumption(HomeApplianceDTO homeApplianceDTO) {
        EnergyConsumption consumption = homeApplianceDTO.getEnergyConsumption();
        try {
            EnergyConsumptionDTO consumptionDTO = EnergyConsumptionDTO.toDTO(consumption);
            consumptionDTO = energyConsumptionService.save(consumptionDTO);
            consumption = EnergyConsumption.builder()
                    .name(consumptionDTO.getName())
                    .power(consumptionDTO.getPower())
                    .monthlyUsage(consumptionDTO.getMonthlyUsage())
                    .dailyUse(consumptionDTO.getDailyUse())
                    .monthlyConsumptionAverage(consumptionDTO.getMonthlyConsumptionAverage())
                    .build();
        } catch (NullPointerException e) {
            log.info("The EnergyConsumption object was null!");
        } catch (ConverterNotFoundException e) {
            log.info("Error when trying to convert energyConsumption to energyConsumptionDTO!");
        }
        homeApplianceDTO.setEnergyConsumption(consumption);
    }

    private void checkApplianceNameValidity(HomeApplianceDTO homeApplianceDTO) {
        Optional<HomeAppliance> appliance = homeApplianceRepository.findByNameIgnoreCase(homeApplianceDTO.getName());
        if (appliance.isPresent()) throw new ExistingResourceException(homeApplianceDTO.getName());
    }

    public void setAttributeDefaultValue(HomeApplianceDTO homeApplianceDTO) {
        if (homeApplianceDTO.getPrice() == null) homeApplianceDTO.setPrice(0.0);
        if (homeApplianceDTO.getInventory() == null) homeApplianceDTO.setInventory(0);
        if (homeApplianceDTO.getId() == null) {
            homeApplianceDTO.setId(UUID.randomUUID().toString());
        } else {
            try {
                if(checkUUID(homeApplianceDTO.getId())) {
                    findById(homeApplianceDTO.getId());
                    throw new ExistingResourceException(homeApplianceDTO.getId());
                } else {
                    homeApplianceDTO.setId(UUID.randomUUID().toString());
                }
            } catch (ResourceNotFoundException e) {
                log.info("The id was not null and it's still not registered in the database. " + e.getClass());
            }
        }
    }
    
    private Boolean checkUUID(String uuid){
        if (uuid.length() == 36) {
            String[] hexadecimalArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
            String[] alphaNumericArray = {"8", "9", "a", "b"};
            String x = uuid.substring(14, 15);
            String y = uuid.substring(19, 20);
            if (Arrays.stream(hexadecimalArray).anyMatch(element -> element.equals(x)) &&
                    Arrays.stream(alphaNumericArray).anyMatch(element -> element.equals(y))) return true;
        }
        return false;
    }

    private HomeAppliance fromHomeApplianceDTO(HomeApplianceDTO homeApplianceDTO) {
        HomeAppliance homeAppliance = HomeAppliance.builder()
                .id(homeApplianceDTO.getId())
                .name(homeApplianceDTO.getName())
                .description(homeApplianceDTO.getDescription())
                .price(homeApplianceDTO.getPrice())
                .inventory(homeApplianceDTO.getInventory())
                .voltage(homeApplianceDTO.getVoltage())
                .portable(homeApplianceDTO.getPortable())
                .classification(homeApplianceDTO.getClassification())
                .energyConsumption(homeApplianceDTO.getEnergyConsumption())
                .build();
        return homeAppliance;
    }
}
