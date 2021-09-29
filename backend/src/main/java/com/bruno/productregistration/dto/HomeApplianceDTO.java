package com.bruno.productregistration.dto;

import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class HomeApplianceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Enter the product name")
    private String name;

    private String description;
    private Double price;
    private Integer inventory;
    private Integer voltage;
    private Boolean portable;
    private Integer classification;
    private EnergyConsumption energyConsumption;

    public HomeApplianceDTO(String name, String description, Double price, Integer inventory, Integer voltage, Boolean portable, Integer classification) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.voltage = voltage;
        this.portable = portable;
        this.classification = classification;
    }

    public HomeApplianceDTO(HomeAppliance homeAppliance){
        name = homeAppliance.getName();
        description = homeAppliance.getDescription();
        price = homeAppliance.getPrice();
        inventory = homeAppliance.getInventory();
        voltage = homeAppliance.getVoltage().getCode();
        portable = homeAppliance.getPortable();
        classification = homeAppliance.getClassification().getCode();
    }
}
