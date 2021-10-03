package com.bruno.productregistration.repositories;

import com.bruno.productregistration.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, String> {

    Optional<EnergyConsumption> findByNameIgnoreCase(String name);
}
