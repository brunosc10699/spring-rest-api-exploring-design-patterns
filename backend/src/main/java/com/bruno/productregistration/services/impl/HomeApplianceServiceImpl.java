package com.bruno.productregistration.services.impl;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.repositories.HomeApplianceRepository;
import com.bruno.productregistration.services.HomeApplianceService;
import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        checkApplianceNameValidity(homeApplianceDTO);
        setAttributeDefaultValue(homeApplianceDTO);
        EnergyConsumption consumption = homeApplianceDTO.getEnergyConsumption();
        try {
            consumption = energyConsumptionService.save(consumption);
        } catch (NullPointerException e) {
            log.info("The EnergyConsumption object was null!");
        }
        homeApplianceDTO.setEnergyConsumption(consumption);
        HomeAppliance product = fromHomeApplianceDTO(homeApplianceDTO);
        product = homeApplianceRepository.save(product);
        return HomeApplianceDTO.builder().build().toDTO(product);
    }

    @Transactional(readOnly = true)
    @Override
    public HomeApplianceDTO findByNameIgnoreCase(String name) {
        HomeAppliance appliance = homeApplianceRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(name));
        return HomeApplianceDTO.builder().build().toDTO(appliance);
    }

    @Transactional(readOnly = true)
    @Override
    public HomeApplianceDTO findById(String id) {
        HomeAppliance appliance = homeApplianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return HomeApplianceDTO.builder().build().toDTO(appliance);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HomeApplianceDTO> findAll(Pageable pageable) {
        return homeApplianceRepository.findAll(pageable)
                .map(element -> HomeApplianceDTO.builder().build().toDTO(element));
    }

    private void checkApplianceNameValidity(HomeApplianceDTO homeApplianceDTO) {
        Optional<HomeAppliance> appliance = homeApplianceRepository.findByNameIgnoreCase(homeApplianceDTO.getName());
        if (appliance.isPresent()) throw new ExistingResourceException(homeApplianceDTO.getName());
    }

    private void setAttributeDefaultValue(HomeApplianceDTO homeApplianceDTO) {
        if(!checkUUID(homeApplianceDTO.getId())) homeApplianceDTO.setId(UUID.randomUUID().toString());
        if (homeApplianceDTO.getPrice() == null) homeApplianceDTO.setPrice(0.0);
        if (homeApplianceDTO.getInventory() == null) homeApplianceDTO.setInventory(0);
    }
    
    private Boolean checkUUID(String uuid){
        String[] hexadecimalArray = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        String[] alphaNumericArray = {"8","9","a","b"};
        String x = uuid.substring(14, 15);
        String y = uuid.substring(19, 20);
        if (Arrays.stream(hexadecimalArray).anyMatch(element -> element.equals(x)) &&
                Arrays.stream(alphaNumericArray).anyMatch(element -> element.equals(y)))
            return true;
        return false;
    }

    private HomeAppliance fromHomeApplianceDTO(HomeApplianceDTO homeApplianceDTO) {
        HomeAppliance product = HomeAppliance.builder()
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
        return product;
    }
}
