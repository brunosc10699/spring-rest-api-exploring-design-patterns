package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.repositories.EnergyConsumptionRepository;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import com.bruno.productregistration.services.impl.EnergyConsumptionServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnergyConsumptionServiceImplTest {

    @Mock
    private EnergyConsumptionRepository energyConsumptionRepository;

    @InjectMocks
    private EnergyConsumptionServiceImpl energyConsumptionService;

    private EnergyConsumption goodObject = EnergyConsumption.builder()
            .name("Vacuum")
            .power(500)
            .build();

    private EnergyConsumptionDTO goodDTO = EnergyConsumptionDTO.toDTO(goodObject);
    
    private EnergyConsumption badObject = EnergyConsumption.builder()
            .power(500).build();

    private EnergyConsumptionDTO badDTO = EnergyConsumptionDTO.toDTO(badObject);

    private EnergyConsumption emptyObject = EnergyConsumption.builder().build();

    @Test
    @DisplayName("(1) When an object that contains a name and a power attribute is given" +
            "then it should be registered")
    void whenAnObjectIsGivenThenRegisterTheObject() {
        when(energyConsumptionRepository.save(goodObject)).thenReturn(goodObject);
        EnergyConsumption consumption = energyConsumptionService.save(goodObject);
        assertAll(
                () -> assertThat(consumption.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(consumption.getPower(), is(equalTo(goodObject.getPower()))),
                () -> assertNull(consumption.getMonthlyUsage()),
                () -> assertNull(consumption.getDailyUse()),
                () -> assertNull(consumption.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(2) When an object doesn't contain a name and a power attribute" +
            "then don't register it. Return a null object.")
    void whenAnObjectDoesNotHaveNameAndPowerAttributeThenDoNotRegisterIt() {
        EnergyConsumption consumption = energyConsumptionService.save(badObject);
        assertNull(consumption);
    }

    @Test
    @DisplayName("(3) When an empty object is given then don't register it. Return a null object.")
    void whenAnEmptyObjectIsGivenThenDoNotRegisterIt() {
        EnergyConsumption consumption = energyConsumptionService.save(emptyObject);
        assertNull(consumption);
    }

    @Test
    @DisplayName("(4) When the name attribute length is less than 3 characters," +
            "then don't register it. Return a null object.")
    void whenNameAttributeLengthIsLessThanThreeCharactersThenDoNotRegisterIt() {
        EnergyConsumption consumption = energyConsumptionService.save(emptyObject);
        assertNull(consumption);
    }

    @Test
    @DisplayName("(5) When a name is given then return an object of EnergyConsumption")
    void whenANameIsGivenThenReturnAnOptionalObject() {
        EnergyConsumption consumption = energyConsumptionService.findByNameIgnoreCase(goodObject);
        assertAll(
                () -> assertThat(consumption.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(consumption.getPower(), is(equalTo(goodObject.getPower())))
        );
    }

    @Test
    @DisplayName("(6) When an object is not found by its name in the database nor in the api, then return the same object")
    void whenAnObjectIsNotFoundThenReturnTheSameObject() {
        EnergyConsumption consumption = energyConsumptionService.findByNameIgnoreCase(goodObject);
        assertAll(
                () -> assertThat(consumption.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(consumption.getPower(), is(equalTo(goodObject.getPower())))
        );
    }

    @Test
    @DisplayName("(7) When a valid id is given to find data, then return an object")
    void whenAValidIdIsGivenThenReturnAnObject() {
        when(energyConsumptionRepository.findById(goodObject.getName())).thenReturn(Optional.of(goodObject));
        EnergyConsumptionDTO consumption = energyConsumptionService.findById(goodDTO.getName());
        assertAll(
                () -> assertThat(consumption.getName(), is(equalTo(goodDTO.getName()))),
                () -> assertThat(consumption.getPower(), is(equalTo(goodDTO.getPower()))),
                () -> assertNull(consumption.getMonthlyUsage()),
                () -> assertNull(consumption.getDailyUse()),
                () -> assertNull(consumption.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(8) When an invalid id is given to find data, then throw a ResourceNotFoundException exception")
    void whenAnInvalidIdIsGivenThenThrowAnException() {
        doThrow(ResourceNotFoundException.class).when(energyConsumptionRepository).findById(badObject.getName());
        assertThrows(ResourceNotFoundException.class, () -> energyConsumptionService.findById(badDTO.getName()));
    }

    @Test
    @DisplayName("(9) When a valid id is given to update data, then return an object")
    void whenAValidIdIsGivenThenUpdateData() {
        when(energyConsumptionRepository.findById(goodObject.getName())).thenReturn(Optional.of(goodObject));
        when(energyConsumptionRepository.save(goodObject)).thenReturn(goodObject);
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.update(goodDTO.getName(), goodDTO);
        assertAll(
                () -> assertThat(consumptionDTO.getName(), is(equalTo(goodDTO.getName()))),
                () -> assertThat(consumptionDTO.getPower(), is(equalTo(goodDTO.getPower()))),
                () -> assertNull(consumptionDTO.getMonthlyUsage()),
                () -> assertNull(consumptionDTO.getDailyUse()),
                () -> assertNull(consumptionDTO.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(10) When an invalid id is given to update data, then throw a ResourceNotFoundException exception")
    void whenAnInvalidIdIsGivenThenThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> energyConsumptionService.update(badDTO.getName(), badDTO));
    }
}
