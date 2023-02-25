package com.example.pharmacyproject.pharmacy.service;

import com.example.pharmacyproject.api.dto.DocumentDto;
import com.example.pharmacyproject.api.dto.KakaoApiResponseDto;
import com.example.pharmacyproject.api.service.KakaoAddressSearchService;
import com.example.pharmacyproject.direction.dto.OutputDto;
import com.example.pharmacyproject.direction.entity.Direction;
import com.example.pharmacyproject.direction.service.Base62Service;
import com.example.pharmacyproject.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    @Value("{pharmacy.recommendation.base.url}")
    private String baseUrl;

    public List<OutputDto> recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtos())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList] Input address : {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtos().get(0);

        List<Direction> directions = directionService.buildDirectionList(documentDto);
//        List<Direction> directions = directionService.buildDirectionListByCategoryApi(documentDto);

        return directionService.saveAll(directions).stream()
                .map(this::converToOutputDto)
                .collect(toList());
    }

    private OutputDto converToOutputDto(Direction direction) {


        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
