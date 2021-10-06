package com.bruno.productregistration.dto;

import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.services.exceptions.IncorrectValueException;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class HomeApplianceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "This field must be filled in!")
    @Length(min = 3, max = 100, message = "This field must have from 3 to 100 characters maximum!")
    private String name;

    private String description;
    private Double price;

    private Integer inventory;

    @NotNull(message = "This field must be filled in with a number!")
    private Integer voltage;

    @NotNull(message = "This field must be filled in with 'true' or 'false'!")
    private Boolean portable;

    private Integer classification;
    private EnergyConsumption energyConsumption;

    public static HomeApplianceDTO toDTO(HomeAppliance homeAppliance) {
        HomeApplianceDTO homeApplianceDTO = HomeApplianceDTO.builder().build();
        try {
            homeApplianceDTO = HomeApplianceDTO.builder()
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
        } catch (IllegalArgumentException e) {
            throw new IncorrectValueException(e.getMessage());
        }
        return homeApplianceDTO;
    }

}
