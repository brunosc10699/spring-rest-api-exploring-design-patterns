package com.bruno.productregistration.dto;

import com.bruno.productregistration.entities.EnergyConsumption;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class EnergyConsumptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "This field must be filled in!")
    @Length(min = 3, max = 100, message = "This field must have from 3 to 100 characters maximum!")
    private String name;

    @EqualsAndHashCode.Exclude
    @NotNull(message = "This field must be filled in with numbers!")
    private Integer power;

    @EqualsAndHashCode.Exclude
    private Integer monthlyUsage;

    @EqualsAndHashCode.Exclude
    private Integer dailyUse;

    @EqualsAndHashCode.Exclude
    private Double monthlyConsumptionAverage;

    public static EnergyConsumptionDTO toDTO(EnergyConsumption energyConsumption){
        return EnergyConsumptionDTO.builder()
                .name(energyConsumption.getName())
                .power(energyConsumption.getPower())
                .monthlyUsage(energyConsumption.getMonthlyUsage())
                .dailyUse(energyConsumption.getDailyUse())
                .monthlyConsumptionAverage(energyConsumption.getMonthlyConsumptionAverage())
                .build();
    }

}
