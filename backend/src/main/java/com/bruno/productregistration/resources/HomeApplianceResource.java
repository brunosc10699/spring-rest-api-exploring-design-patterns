package com.bruno.productregistration.resources;

import com.bruno.productregistration.dto.HomeApplianceDTO;
import com.bruno.productregistration.services.impl.HomeApplianceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/appliances")
public class HomeApplianceResource {

    private final HomeApplianceServiceImpl homeApplianceService;

    @PostMapping
    public ResponseEntity<HomeApplianceDTO> save(@Valid @RequestBody HomeApplianceDTO product) {
        product = homeApplianceService.save(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @GetMapping
    public ResponseEntity<Page<HomeApplianceDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(homeApplianceService.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HomeApplianceDTO> findById(@PathVariable String id){
        return ResponseEntity.ok(homeApplianceService.findById(id));
    }
}
