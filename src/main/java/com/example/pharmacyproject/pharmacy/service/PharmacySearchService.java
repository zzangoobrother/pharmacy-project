package com.example.pharmacyproject.pharmacy.service;

import com.example.pharmacyproject.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacyproject.pharmacy.dto.PharmacyDto;
import com.example.pharmacyproject.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList() {
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if (!pharmacyDtoList.isEmpty()) {
            return pharmacyDtoList;
        }

        return pharmacyRepositoryService.findAll().stream()
                .map(this::convertToPharmacyDto)
                .collect(toList());
    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
