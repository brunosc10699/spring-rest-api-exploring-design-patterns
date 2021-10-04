package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.repositories.HomeApplianceRepository;
import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import com.bruno.productregistration.services.impl.HomeApplianceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeApplianceServiceTest {

    @Mock
    private HomeApplianceRepository homeApplianceRepository;

    @InjectMocks
    private HomeApplianceServiceImpl homeApplianceService;

    private HomeAppliance goodObject = HomeAppliance.builder()
            .id("0bd985cd-bc4e-484e-ab9d-721f63d7c908")
            .name("Vacuum")
            .description(null)
            .price(0.0)
            .inventory(0)
            .classification(3)
            .voltage(0)
            .portable(true)
            .energyConsumption(null)
            .build();

    private HomeApplianceDTO goodDTO = HomeApplianceDTO.builder().build().toDTO(goodObject);

    private HomeAppliance badObject = HomeAppliance.builder()
            .id("0bd985cd-bc4e-484e-ab9d-721f63d7c908")
            .name("Va")
            .price(0.0)
            .inventory(0)
            .portable(true)
            .classification(3)
            .voltage(0)
            .build();

    private HomeApplianceDTO badDTO = HomeApplianceDTO.builder().build().toDTO(badObject);

    @Test
    @DisplayName("(1) When giving a good HomeApplianceDTO object, then register a new product")
    void whenGivingAGoodHomeApplianceDTOObjectThenSaveANewProduct() {
        when(homeApplianceRepository.save(goodObject)).thenReturn(goodObject);
        HomeApplianceDTO applianceDTO = homeApplianceService.save(goodDTO);
        assertAll(
                () -> assertNotNull(applianceDTO.getId()),
                () -> assertThat(applianceDTO.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(applianceDTO.getPrice(), is(equalTo(goodDTO.getPrice()))),
                () -> assertThat(applianceDTO.getInventory(), is(equalTo(goodDTO.getInventory()))),
                () -> assertThat(applianceDTO.getPortable(), is(equalTo(goodDTO.getPortable()))),
                () -> assertNull(applianceDTO.getDescription()),
                () -> assertThat(applianceDTO.getClassification(), is(equalTo(3))),
                () -> assertThat(applianceDTO.getVoltage(), is(equalTo(0))),
                () -> assertNull(applianceDTO.getEnergyConsumption())
        );
    }

    @Test
    @DisplayName("(2)When trying to save an appliance with a registered name in the database," +
            "then throw ExistingResourceException")
    void whenTryingToSaveAnApplianceThenThrowExistingResourceException() {
        doThrow(ExistingResourceException.class).when(homeApplianceRepository).save(goodObject);
        assertThrows(ExistingResourceException.class, () -> homeApplianceService.save(goodDTO));
    }

    @Test
    @DisplayName("(3) When giving a name then return a HomeApplianceDTO object")
    void whenGivingANameThenReturnADTOObject() {
        when(homeApplianceRepository.findByNameIgnoreCase(goodObject.getName())).thenReturn(Optional.of(goodObject));
        HomeApplianceDTO applianceDTO = homeApplianceService.findByNameIgnoreCase(goodDTO.getName());
        assertAll(
                () -> assertNotNull(applianceDTO.getId()),
                () -> assertThat(applianceDTO.getName(), is(equalTo(goodDTO.getName())))
        );
    }

    @Test
    @DisplayName("(4) When giving an unknown name then throw a ResourceNotFoundException")
    void whenGivingAnUnknownNameThenThrowAnException() {
        doThrow(ResourceNotFoundException.class).when(homeApplianceRepository).findByNameIgnoreCase(badObject.getName());
        assertThrows(ResourceNotFoundException.class, () -> homeApplianceService.findByNameIgnoreCase(badDTO.getName()));
    }

    @Test
    @DisplayName("() When giving a bad HomeApplianceDTO object, then throw a validation exception")
    void whenGivingABadHomeApplianceDTOObjectThenThrowAnException() {
    }

}
