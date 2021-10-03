package com.bruno.productregistration.entities;

import com.bruno.productregistration.entities.enums.Classification;
import com.bruno.productregistration.entities.enums.Voltage;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tb_home_appliance")
@SuperBuilder
@JsonTypeName("homeAppliance")
public class HomeAppliance extends Product {

    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    private Boolean portable;

    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumn(name = "energy_consumption_name")
    private EnergyConsumption energyConsumption;

    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    private Integer voltage;

    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    private Integer classification;

    HomeAppliance() {
    }

    HomeAppliance(String id, String name, String description, Double price, Integer inventory, Boolean portable, EnergyConsumption energyConsumption, Voltage voltage, Classification classification) {
        super(id, name, description, price, inventory);
        this.portable = portable;
        this.energyConsumption = energyConsumption;
        this.voltage = (voltage == null) ? null : voltage.getCode();
        this.classification = (classification == null) ? null : classification.getCode();
    }

    public Boolean getPortable() {
        return portable;
    }

    public void setPortable(Boolean portable) {
        this.portable = portable;
    }

    public EnergyConsumption getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(EnergyConsumption energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public Voltage getVoltage() {
        return Voltage.toEnum(voltage);
    }

    public void setVoltage(Voltage voltage) {
        this.voltage = voltage.getCode();
    }

    public Classification getClassification() {
        return Classification.toEnum(classification);
    }

    public void setClassification(Classification classification) {
        this.classification = classification.getCode();
    }
}
