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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnergyConsumptionServiceTest {

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
            .name("Va")
            .power(500)
            .build();

    private EnergyConsumptionDTO badDTO = EnergyConsumptionDTO.toDTO(badObject);

    private EnergyConsumption emptyObject = EnergyConsumption.builder().build();

    private EnergyConsumptionDTO emptyDTO = EnergyConsumptionDTO.toDTO(emptyObject);

    @Test
    @DisplayName("(1) When an object that contains a name and a power attribute is given" +
            "then it should be registered")
    void whenAnObjectIsGivenThenRegisterTheObject() {
        when(energyConsumptionRepository.save(goodObject)).thenReturn(goodObject);
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.save(goodDTO);
        assertAll(
                () -> assertThat(consumptionDTO.getName(), is(equalTo(goodDTO.getName()))),
                () -> assertThat(consumptionDTO.getPower(), is(equalTo(goodDTO.getPower()))),
                () -> assertNull(consumptionDTO.getMonthlyUsage()),
                () -> assertNull(consumptionDTO.getDailyUse()),
                () -> assertNull(consumptionDTO.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(2) When an object doesn't contain a name and a power attribute" +
            "then don't register it.")
    void whenAnObjectDoesNotHaveANameAndAPowerAttributeThenDoNotRegisterIt() {
        EnergyConsumption incompleteObject = EnergyConsumption.builder().monthlyUsage(4).build();
        when(energyConsumptionRepository.save(incompleteObject)).thenReturn(emptyObject);
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.save(emptyDTO);
        assertAll(
                () -> assertNull(consumptionDTO.getName()),
                () -> assertNull(consumptionDTO.getPower()),
                () -> assertNull(consumptionDTO.getMonthlyUsage()),
                () -> assertNull(consumptionDTO.getDailyUse()),
                () -> assertNull(consumptionDTO.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(3) When an empty object is given then don't register it. Return a null object.")
    void whenAnEmptyObjectIsGivenThenDoNotRegisterIt() {
        when(energyConsumptionRepository.save(emptyObject)).thenReturn(emptyObject);
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.save(emptyDTO);
        assertAll(
                () -> assertNull(consumptionDTO.getName()),
                () -> assertNull(consumptionDTO.getPower()),
                () -> assertNull(consumptionDTO.getMonthlyUsage()),
                () -> assertNull(consumptionDTO.getDailyUse()),
                () -> assertNull(consumptionDTO.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(4) When the name's attribute length is less than 3 characters," +
            "then don't register it. Return the same object.")
    void whenNameAttributeLengthIsLessThanThreeCharactersThenDoNotRegisterIt() {
//        when(energyConsumptionRepository.save(badObject)).thenReturn(badObject);
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.save(badDTO);
        assertAll(
                () -> assertNull(consumptionDTO.getName()),
                () -> assertNull(consumptionDTO.getPower()),
                () -> assertNull(consumptionDTO.getMonthlyUsage()),
                () -> assertNull(consumptionDTO.getDailyUse()),
                () -> assertNull(consumptionDTO.getMonthlyConsumptionAverage())
        );
    }

    @Test
    @DisplayName("(5) When a name is given then return an object of EnergyConsumption")
    void whenANameIsGivenThenReturnAnOptionalObject() {
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.findByNameIgnoreCase(goodDTO);
        assertAll(
                () -> assertThat(consumptionDTO.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(consumptionDTO.getPower(), is(equalTo(goodObject.getPower())))
        );
    }

    @Test
    @DisplayName("(6) When an object is not found by its name in the database nor in the api, then return the same object")
    void whenAnObjectIsNotFoundThenReturnTheSameObject() {
        EnergyConsumptionDTO consumptionDTO = energyConsumptionService.findByNameIgnoreCase(goodDTO);
        assertAll(
                () -> assertThat(consumptionDTO.getName(), is(equalTo(goodObject.getName()))),
                () -> assertThat(consumptionDTO.getPower(), is(equalTo(goodObject.getPower())))
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
    void whenAnInvalidIdIsGivenToUpdateThenThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> energyConsumptionService.update(badDTO.getName(), badDTO));
    }

    @Test
    @DisplayName("(11) When a valid id is given to delete data, then delete it")
    void whenAValidIdIsGivenThenDeleteData() {
        when(energyConsumptionRepository.findById(goodObject.getName())).thenReturn(Optional.of(goodObject));
        doNothing().when(energyConsumptionRepository).deleteById(goodObject.getName());
        energyConsumptionService.delete(goodDTO.getName());
        verify(energyConsumptionRepository, times(1)).findById(goodObject.getName());
        verify(energyConsumptionRepository, times(1)).deleteById(goodObject.getName());
    }

    @Test
    @DisplayName("(12) When an invalid id is given to delete data, then throw a ResourceNotFoundException exception")
    void whenAnInvalidIdIsGivenToDeleteThenThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> energyConsumptionService.delete(badDTO.getName()));
    }

    @Test
    @DisplayName("(13) When searching for all energy consumption data, then return a page")
    void whenFindAllServiceMethodIsCalledThenReturnAPage() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page page = new PageImpl(Collections.singletonList(goodObject));
        when(energyConsumptionRepository.findAll(pageRequest)).thenReturn(page);
        Page<EnergyConsumptionDTO> newPage = energyConsumptionService.findAll(pageRequest);
        assertAll(
                () -> assertThat(newPage.getContent(), is(not(empty()))),
                () -> assertThat(newPage.getTotalElements(), is(equalTo(1L))),
                () -> assertThat(newPage.getTotalPages(), is(equalTo(1))),
                () -> assertThat(newPage.getSize(), is(equalTo(1))),
                () -> assertThat(newPage.getContent().get(0), is(equalTo(goodDTO)))
        );
    }

    @Test
    @DisplayName("(14) When searching for all energy consumption data, then return an empty page")
    void whenFindAllServiceMethodIsCalledThenReturnAnEmptyPage() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page page = new PageImpl(Collections.emptyList());
        when(energyConsumptionRepository.findAll(pageRequest)).thenReturn(page);
        Page<EnergyConsumptionDTO> newPage = energyConsumptionService.findAll(pageRequest);
        assertThat(newPage.getContent(), is(empty()));
    }
}
