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

        if (!activeProfile.equals("prod")) {
            EnergyConsumption consumption = EnergyConsumption.builder()
                    .name("Enceradeira")
                    .power(180)
                    .monthlyUsage(4)
                    .dailyUse(120)
                    .monthlyConsumptionAverage(16.5)
                    .build();
            energyConsumptionRepository.save(consumption);

            HomeAppliance homeAppliance = HomeAppliance.builder()
                    .id("eba9031b-b87d-4b91-9afd-9a1e0387a9bf")
                    .name("Enceradeira")
                    .description("Enceradeira para piso sintético")
                    .price(0.0)
                    .inventory(0)
                    .voltage(0)
                    .portable(true)
                    .classification(2)
                    .energyConsumption(consumption)
                    .build();
            homeApplianceRepository.save(homeAppliance);
        }
    }
}
