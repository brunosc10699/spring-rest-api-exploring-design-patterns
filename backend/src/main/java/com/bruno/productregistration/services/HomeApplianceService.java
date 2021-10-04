package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeApplianceService {

    HomeApplianceDTO save(HomeApplianceDTO homeApplianceDTO);

    HomeApplianceDTO findByNameIgnoreCase(String name);

    HomeApplianceDTO findById(String id);

    Page<HomeApplianceDTO> findAll(Pageable pageable);

    HomeApplianceDTO update(String id, HomeApplianceDTO homeApplianceDTO);
}
