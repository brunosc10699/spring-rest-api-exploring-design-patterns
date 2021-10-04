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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    @DisplayName("(5) When giving a valid id, then return a HomeApplianceDTO object")
    void whenGivingAValidIDThenReturnAHomeApplianceDTOObject() {
        when(homeApplianceRepository.findById(goodObject.getId())).thenReturn(Optional.of(goodObject));
        HomeApplianceDTO applianceDTO = homeApplianceService.findById(goodDTO.getId());
        assertAll(
                () -> assertNotNull(applianceDTO),
                () -> assertThat(applianceDTO.getId(), is(equalTo(goodDTO.getId()))),
                () -> assertThat(applianceDTO.getName(), is(equalTo(goodDTO.getName())))
        );
    }

    @Test
    @DisplayName("(6) When giving an invalid id, then throw a ResourceNotFoundException")
    void whenGivingAnInvalidIDThenThrowAnException() {
        doThrow(ResourceNotFoundException.class).when(homeApplianceRepository).findById(badObject.getId());
        assertThrows(ResourceNotFoundException.class, () -> homeApplianceService.findById(badDTO.getId()));
    }

    @Test
    @DisplayName("(7) When searching for all products, then return a page of them")
    void whenRequestingAllProductsThenReturnAPageOfThem() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<HomeAppliance> page = new PageImpl<>(Collections.singletonList(goodObject));
        when(homeApplianceRepository.findAll(pageRequest)).thenReturn(page);
        Page<HomeApplianceDTO> newPage = homeApplianceService.findAll(pageRequest);
        assertAll(
                () -> assertThat(newPage.getContent(), is(not(empty()))),
                () -> assertThat(newPage.getTotalPages(), is(equalTo(1))),
                () -> assertThat(newPage.getSize(), is(equalTo(1))),
                () -> assertThat(newPage.getTotalElements(), is(equalTo(1L))),
                () -> assertThat(newPage.getContent().get(0), is(equalTo(goodDTO)))
        );
    }

    @Test
    @DisplayName("(8) When searching for all products, then return an empty page")
    void whenRequestingAllProductsThenReturnAnEmptyPage() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<HomeAppliance> page = new PageImpl<>(Collections.emptyList());
        when(homeApplianceRepository.findAll(pageRequest)).thenReturn(page);
        Page<HomeApplianceDTO> newPage = homeApplianceService.findAll(pageRequest);
        assertThat(newPage.getContent(), is(empty()));
    }
}
