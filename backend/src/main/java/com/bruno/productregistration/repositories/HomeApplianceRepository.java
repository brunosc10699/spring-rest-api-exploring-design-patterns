package com.bruno.productregistration.repositories;

import com.bruno.productregistration.entities.HomeAppliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeApplianceRepository extends JpaRepository<HomeAppliance, String> {

    Optional<HomeAppliance> findByNameIgnoreCase(String name);
}
