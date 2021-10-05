package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.services.impl.HomeApplianceServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HomeApplianceResourceTest {

    private static final String URN = "/api/v1/appliances/";

    private String id = "0bd985cd-bc4e-484e-ab9d-721f63d7c908";

    private HomeAppliance goodObject = HomeAppliance.builder()
            .id(id)
            .name("Vacuum")
            .description(null)
            .price(0.0)
            .inventory(0)
            .classification(3)
            .voltage(0)
            .portable(true)
            .energyConsumption(null)
            .build();

    private HomeApplianceDTO goodDTO = HomeApplianceDTO.toDTO(goodObject);

    private HomeAppliance badObject = HomeAppliance.builder()
            .id(id)
            .name("Va")
            .price(0.0)
            .inventory(0)
            .portable(true)
            .classification(3)
            .voltage(0)
            .build();

    private HomeApplianceDTO badDTO = HomeApplianceDTO.toDTO(badObject);

    @Mock
    private HomeApplianceServiceImpl homeApplianceService;

    @InjectMocks
    private HomeApplianceResource homeApplianceResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(homeApplianceResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("(1) When POST is called then a new product is created")
    void whenPOSTIsCalledThenAProductIsCreated() throws Exception {
        when(homeApplianceService.save(goodDTO)).thenReturn(goodDTO);
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(goodDTO.getName())))
                .andExpect(jsonPath("$.description", is(goodDTO.getDescription())))
                .andExpect(jsonPath("$.price", is(goodDTO.getPrice())))
                .andExpect(jsonPath("$.inventory", is(goodDTO.getInventory())))
                .andExpect(jsonPath("$.voltage", is(goodDTO.getVoltage())))
                .andExpect(jsonPath("$.portable", is(goodDTO.getPortable())))
                .andExpect(jsonPath("$.classification", is(goodDTO.getClassification())))
                .andExpect(jsonPath("$.energyConsumption", is(goodDTO.getEnergyConsumption())));
    }

    @Test
    @DisplayName("(2) When POST is called without required fields, then return an error")
    void whenPOSTIsCalledWithoutRequiredFieldsThenReturnAnError() throws Exception {
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(badDTO)))
                .andExpect(status().isBadRequest());
    }
}
