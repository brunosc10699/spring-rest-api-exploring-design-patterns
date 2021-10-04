package com.bruno.productregistration.services;

import com.bruno.productregistration.dto.HomeApplianceDTO;

public interface HomeApplianceService {

    HomeApplianceDTO save(HomeApplianceDTO homeApplianceDTO);

    HomeApplianceDTO findByNameIgnoreCase(String name);

    HomeApplianceDTO findById(String id);
}
