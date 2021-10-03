package com.bruno.productregistration.dto;

import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class HomeApplianceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "This field must be filled in!")
    @Size(min = 3, max = 100, message = "This field must have from 3 to 100 characters maximum!")
    private String name;

    private String description;
    private Double price;

    @NotNull(message = "This field must be filled in with a number!")
    private Integer inventory;

    @NotNull(message = "This field must be filled in with a number!")
    private Integer voltage;

    @NotNull(message = "This field must be filled in with 'true' or 'false'!")
    private Boolean portable;

    private Integer classification;
    private EnergyConsumption energyConsumption;

    public HomeApplianceDTO toDTO(HomeAppliance homeAppliance){
        return HomeApplianceDTO.builder()
                .id(homeAppliance.getId())
                .name(homeAppliance.getName())
                .description(homeAppliance.getDescription())
                .price(homeAppliance.getPrice())
                .inventory(homeAppliance.getInventory())
                .voltage(homeAppliance.getVoltage().getCode())
                .portable(homeAppliance.getPortable())
                .classification(homeAppliance.getClassification().getCode())
                .energyConsumption(homeAppliance.getEnergyConsumption())
                .build();
    }

}
