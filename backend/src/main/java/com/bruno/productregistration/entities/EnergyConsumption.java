package com.bruno.productregistration.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_energy_consumption")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyConsumption {

    @Id
    private String name;

    @EqualsAndHashCode.Exclude
    private Integer power;

    @EqualsAndHashCode.Exclude
    private Integer monthlyUsage;

    @EqualsAndHashCode.Exclude
    private Integer dailyUse;

    @EqualsAndHashCode.Exclude
    private Double monthlyConsumptionAverage;
}
