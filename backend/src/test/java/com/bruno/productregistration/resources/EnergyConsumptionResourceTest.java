package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.EnergyConsumptionDTO;
import com.bruno.productregistration.services.EnergyConsumptionService;
import com.bruno.productregistration.services.exceptions.ExistingResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.bruno.productregistration.resources.utils.JsonConversionUtil.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EnergyConsumptionResourceTest {

    private static final String URN = "/api/v1/consumptions/";

    private String id = "0bd985cd-bc4e-484e-ab9d-721f63d7c908";

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
}
