package com.bruno.productregistration.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_energy_consumption")
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class EnergyConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

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
