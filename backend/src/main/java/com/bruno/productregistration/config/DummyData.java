package com.bruno.productregistration.config;

import com.bruno.productregistration.entities.EnergyConsumption;
import com.bruno.productregistration.entities.HomeAppliance;
import com.bruno.productregistration.repositories.EnergyConsumptionRepository;
import com.bruno.productregistration.repositories.HomeApplianceRepository;
import com.bruno.productregistration.services.EnergyConsumptionAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final EnergyConsumptionAPIService energyConsumptionAPIService;

    private final EnergyConsumptionRepository energyConsumptionRepository;

    private final HomeApplianceRepository homeApplianceRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void run(String... args) throws Exception {

        if (activeProfile != "prod") {
            EnergyConsumption energyConsumption = energyConsumptionAPIService.getConsumption("Enceradeira");
            energyConsumptionRepository.save(energyConsumption);

            HomeAppliance homeAppliance = HomeAppliance.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Enceradeira")
                    .description("Enceradeira para piso sintético")
                    .price(0.0)
                    .inventory(0)
                    .voltage(0)
                    .portable(true)
                    .classification(2)
                    .energyConsumption(energyConsumption)
                    .build();
            homeApplianceRepository.save(homeAppliance);
        }
    }
}
