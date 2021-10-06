package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.services.EnergyConsumptionService;
import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.bruno.productregistration.resources.utils.JsonConversionUtil.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EnergyConsumptionResourceTest {

    private static final String URN = "/api/v1/consumptions/";

    private EnergyConsumptionDTO goodDTO = EnergyConsumptionDTO.builder()
            .name("Vacuum")
            .power(200)
            .monthlyUsage(8)
            .dailyUse(120)
            .monthlyConsumptionAverage(15.6)
            .build();

    private EnergyConsumptionDTO badDTO = EnergyConsumptionDTO.builder()
            .name("Va")
            .build();

    @Mock
    private EnergyConsumptionService energyConsumptionService;

    @InjectMocks
    private EnergyConsumptionResource energyConsumptionResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(energyConsumptionResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("(1) When POST is called with a good payload then return 201 created status")
    void whenPOSTIsCalledThenReturnCreatedStatus() throws Exception {
        when(energyConsumptionService.save(goodDTO)).thenReturn(goodDTO);
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(goodDTO.getName())))
                .andExpect(jsonPath("$.power", is(goodDTO.getPower())))
                .andExpect(jsonPath("$.monthlyUsage", is(goodDTO.getMonthlyUsage())))
                .andExpect(jsonPath("$.dailyUse", is(goodDTO.getDailyUse())))
                .andExpect(jsonPath("$.monthlyConsumptionAverage", is(goodDTO.getMonthlyConsumptionAverage())));
    }

    @Test
    @DisplayName("(2) When POST is called with a bad payload then return 400 bad request status")
    void whenPOSTIsCalledThenReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(badDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(3) When POST is called with an existent id then return 400 bad request status")
    void whePOSTIsCalledWithAnExistentIdThenReturnBadRequestStatus() throws Exception {
        EnergyConsumptionDTO registeredName = EnergyConsumptionDTO.builder().name("Vacuum").power(200).build();
        doThrow(ExistingResourceException.class).when(energyConsumptionService).save(registeredName);
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registeredName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(3) When GET is called to find all energy consumption data then return 200 Ok status")
    void whenGETIsCalledToFindAllEnergyConsumptionDataThenReturnOkStatus() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<EnergyConsumptionDTO> page = new PageImpl(Collections.singletonList(goodDTO));
        when(energyConsumptionService.findAll(pageRequest)).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get(URN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("(5) When GET is called to find energy consumption data by a valid id then return 200 Ok Status")
    void whenGETIsCalledToFindDataByAValidIdThenReturnOkStatus() throws Exception {
        when(energyConsumptionService.findById(goodDTO.getName())).thenReturn(goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/id/" + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(goodDTO.getName())))
                .andExpect(jsonPath("$.power", is(goodDTO.getPower())))
                .andExpect(jsonPath("$.monthlyUsage", is(goodDTO.getMonthlyUsage())))
                .andExpect(jsonPath("$.dailyUse", is(goodDTO.getDailyUse())))
                .andExpect(jsonPath("$.monthlyConsumptionAverage", is(goodDTO.getMonthlyConsumptionAverage())));
    }

    @Test
    @DisplayName("(6) When GET is called to find energy consumption data by an invalid id then return 404 Not Found Status")
    void whenGETIsCalledWithAnInvalidIdThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(energyConsumptionService).findById(badDTO.getName());
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/id/" + badDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("(7) When PUT is called to update energy consumption data by a valid id then return 200 Ok status")
    void whenPUTIsCalledToUpdateEnergyConsumptionDataWithAValidIdThenReturnOkStatus() throws Exception {
        when(energyConsumptionService.update(goodDTO.getName(), goodDTO)).thenReturn(goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(URN + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(goodDTO.getName())))
                .andExpect(jsonPath("$.power", is(goodDTO.getPower())))
                .andExpect(jsonPath("$.monthlyUsage", is(goodDTO.getMonthlyUsage())))
                .andExpect(jsonPath("$.dailyUse", is(goodDTO.getDailyUse())))
                .andExpect(jsonPath("$.monthlyConsumptionAverage", is(goodDTO.getMonthlyConsumptionAverage())));
    }

    @Test
    @DisplayName("(8) When PUT is called to update energy consumption data by an invalid id then return 404 Not Found status")
    void whenPUTIsCalledWithAnInvalidIdThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(energyConsumptionService).update(goodDTO.getName(), goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(URN + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("(9) When PUT is called to update energy consumption with a bad payload then return 400 Bad Request status")
    void whenPUTIsCalledToUpdateDataWithABadPayloadThenReturnBadRequestStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URN + badDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(badDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(10) When DELETE is called with a valid id then return 204 No Content status")
    void whenDELETEIsCalledWithAValidIdThenReturnNoContentStatus() throws Exception {
        doNothing().when(energyConsumptionService).delete(goodDTO.getName());
        mockMvc.perform(MockMvcRequestBuilders.delete(URN + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("(11) When DELETE is called with an invalid id then return 404 Not Found status")
    void whenDELETEIsCalledWithAnInvalidIdThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(energyConsumptionService).delete(goodDTO.getName());
        mockMvc.perform(MockMvcRequestBuilders.delete(URN + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
