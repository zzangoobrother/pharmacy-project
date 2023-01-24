package com.example.pharmacyproject.direction.service;

import com.example.pharmacyproject.api.dto.DocumentDto;
import com.example.pharmacyproject.api.service.KakaoCategorySearchService;
import com.example.pharmacyproject.direction.entity.Direction;
import com.example.pharmacyproject.direction.repository.DirectionRepository;
import com.example.pharmacyproject.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectionService {

    // 최대 검색 갯수
    private static final int MAX_SEARCH_COUND = 3;

    // 반경 10KM 이내
    private static final double RADIUS_KM = 10.0;

    private final PharmacySearchService pharmacySearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final DirectionRepository directionRepository;

    @Transactional
    public List<Direction> saveAll(List<Direction> directions) {
        if (CollectionUtils.isEmpty(directions)) {
            return Collections.emptyList();
        }

        return directionRepository.saveAll(directions);
    }

    public List<Direction> buildDirectionList(DocumentDto documentDto) {
        if (Objects.isNull(documentDto)) {
            return Collections.emptyList();
        }

        return pharmacySearchService.searchPharmacyDtoList().stream()
                .map(pharmacyDto -> Direction.builder()
                        .inputAddress(documentDto.getAddressName())
                        .inputLatitude(documentDto.getLatitude())
                        .inputLongitude(documentDto.getLongitude())
                        .targetPharmacyName(pharmacyDto.getPharmacyName())
                        .targetAddress(pharmacyDto.getPharmacyAddress())
                        .targetLatitude(pharmacyDto.getLatitude())
                        .targetLongitude(pharmacyDto.getLongitude())
                        .distance(calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(), pharmacyDto.getLatitude(), pharmacyDto.getLongitude()))
                        .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUND)
                .collect(toList());

    }

    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if (Objects.isNull(inputDocumentDto)) {
            return Collections.emptyList();
        }

        return kakaoCategorySearchService.requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM).getDocumentDtos().stream()
                .map(documentDto -> Direction.builder()
                        .inputAddress(inputDocumentDto.getAddressName())
                        .inputLatitude(inputDocumentDto.getLatitude())
                        .inputLongitude(inputDocumentDto.getLongitude())
                        .targetPharmacyName(documentDto.getAddressName())
                        .targetLatitude(documentDto.getLatitude())
                        .targetLongitude(documentDto.getLongitude())
                        .distance(documentDto.getDistance() * 0.001)
                        .build())
                .limit(MAX_SEARCH_COUND)
                .collect(toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371;
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
