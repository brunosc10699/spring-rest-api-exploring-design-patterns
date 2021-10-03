package com.bruno.productregistration.services;

import com.bruno.productregistration.entities.EnergyConsumption;

public interface EnergyConsumptionService {

    EnergyConsumption save(EnergyConsumption consumption);

    EnergyConsumption findByNameIgnoreCase(EnergyConsumption consumption);
}
