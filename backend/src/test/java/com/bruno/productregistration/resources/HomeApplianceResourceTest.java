package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.services.exceptions.ResourceNotFoundException;
import com.bruno.productregistration.services.impl.HomeApplianceServiceImpl;
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
public class HomeApplianceResourceTest {

    private static final String URN = "/api/v1/appliances/";

    private String id = "0bd985cd-bc4e-484e-ab9d-721f63d7c908";

    private HomeApplianceDTO goodDTO = HomeApplianceDTO.builder()
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

    private HomeApplianceDTO badDTO = HomeApplianceDTO.builder()
            .id(id)
            .name("Va")
            .price(0.0)
            .inventory(0)
            .portable(true)
            .classification(3)
            .voltage(0)
            .build();

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
    @DisplayName("(2) When POST is called without required fields, then return a 400 bad request status")
    void whenPOSTIsCalledWithoutRequiredFieldsThenReturnAnError() throws Exception {
        mockMvc.perform(post(URN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(badDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(3) When GET is called to retrieve all appliances, then OK status is returned")
    void whenGETIsCalledToRetrieveAllAppliancesThenReturnOkStatus() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<HomeApplianceDTO> page = new PageImpl<>(Collections.singletonList(goodDTO));
        when(homeApplianceService.findAll(pageRequest)).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get(URN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("(4) When GET is called to find a product by id, then OK status is returned")
    void whenGETIsCalledToFindAProductByIdThenReturnOkStatus() throws Exception {
        when(homeApplianceService.findById(goodDTO.getId())).thenReturn(goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/id/" + goodDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goodDTO.getId())))
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
    @DisplayName("(5) When GET is called with an invalid id, then return a 404 not found status")
    void whenGETIsCalledWithAnInvalidIdThenReturnNotFoundStatus() throws Exception{
        doThrow(ResourceNotFoundException.class).when(homeApplianceService).findById(badDTO.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/id/" + badDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("(6) When GET is called to find a product by its name, then return 200 Ok status")
    void whenGETIsCalledToFindAProductByItsNameThenReturnOkStatus() throws Exception {
        when(homeApplianceService.findByNameIgnoreCase(goodDTO.getName())).thenReturn(goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/name/" + goodDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goodDTO.getId())))
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
    @DisplayName("(7) When GET is called to find a product with an invalid name, " +
            "then return 404 not found status")
    void whenGETIsCalledToFindAProductWithAnInvalidNameThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(homeApplianceService).findByNameIgnoreCase(badDTO.getName());
        mockMvc.perform(MockMvcRequestBuilders.get(URN + "/name/" + badDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("(8) When PUT is called to update a product with a valid id, then return 200 Ok status")
    void whenPUTIsCalledToUpdateAProductThenReturnOkStatus() throws Exception {
        when(homeApplianceService.update(goodDTO.getId(), goodDTO)).thenReturn(goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(URN + goodDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(goodDTO.getId())))
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
    @DisplayName("(9) When PUT is called to update a product with an invalid id, " +
            "then return 404 not found status")
    void whenPUTIsCalledToUpdateAProductThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(homeApplianceService).update(goodDTO.getId(), goodDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(URN + goodDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goodDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("(10) When PUT is called to update a product with an invalid payload, " +
            "then return 400 bad request status")
    void whenPUTIsCalledToUpdateAProductThenReturnBadRequestStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URN + badDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(badDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("(11) When DELETE is called with a valid id, then return 204 no content status")
    void whenDELETEIsCalledWithAValidIdThenReturnNoContentStatus() throws Exception {
        doNothing().when(homeApplianceService).delete(goodDTO.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete(URN + goodDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("(12) When DELETE is called with an invalid id, then return 404 not found status")
    void whenDELETEIsCalledWithAnInvalidIdThenReturnNotFoundStatus() throws Exception {
        doThrow(ResourceNotFoundException.class).when(homeApplianceService).delete(badDTO.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete(URN + badDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
