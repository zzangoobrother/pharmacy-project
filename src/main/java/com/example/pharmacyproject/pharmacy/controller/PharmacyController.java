package com.example.pharmacyproject.pharmacy.controller;

import com.example.pharmacyproject.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacyproject.pharmacy.dto.PharmacyDto;
import com.example.pharmacyproject.pharmacy.service.PharmacyRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    // 데이터 초기 세팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {
        List<PharmacyDto> pharmacyDtos = pharmacyRepositoryService.findAll().stream()
                .map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .collect(toList());

        pharmacyDtos.forEach(pharmacyRedisTemplateService::save);

        return "success";
    }
}
