package com.example.pharmacyproject.pharmacy.service;

import com.example.pharmacyproject.api.dto.DocumentDto;
import com.example.pharmacyproject.api.dto.KakaoApiResponseDto;
import com.example.pharmacyproject.api.service.KakaoAddressSearchService;
import com.example.pharmacyproject.direction.entity.Direction;
import com.example.pharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtos())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList] Input address : {}", address);
            return;
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtos().get(0);

//        List<Direction> directions = directionService.buildDirectionList(documentDto);
        List<Direction> directions = directionService.buildDirectionListByCategoryApi(documentDto);

        directionService.saveAll(directions);
    }
}
