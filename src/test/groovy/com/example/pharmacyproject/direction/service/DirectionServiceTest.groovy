package com.example.pharmacyproject.direction.service

import com.example.pharmacyproject.api.dto.DocumentDto
import com.example.pharmacyproject.api.service.KakaoCategorySearchService
import com.example.pharmacyproject.direction.repository.DirectionRepository
import com.example.pharmacyproject.pharmacy.dto.PharmacyDto
import com.example.pharmacyproject.pharmacy.service.PharmacySearchService
import spock.lang.Specification

class DirectionServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService = Mock()
    private DirectionRepository directionRepository = Mock()
    private KakaoCategorySearchService kakaoCategorySearchService = Mock()
    private Base62Service base62Service = Mock()

    private DirectionService directionService = new DirectionService(pharmacySearchService, kakaoCategorySearchService, base62Service, directionRepository)

    private List<PharmacyDto> pharmacyDtoList

    def setup() {
        pharmacyDtoList = new ArrayList<>()
        pharmacyDtoList.addAll(
                PharmacyDto.builder()
                .id(1L)
                .pharmacyName("돌곳이온누리약국")
                .pharmacyAddress("주소1")
                .latitude(37.61040424)
                .longitude(127.0569046)
                .build(),
                PharmacyDto.builder()
                .id(2L)
                .pharmacyName("호수온누리약국")
                .pharmacyAddress("주소2")
                .latitude(37.60894036)
                .longitude(127.029052)
                .build()
        )
    }

    def "buildDirectionList - 결과 값이 거리 순으로 정렬이 되는지 확인"() {
        given:
        def addressName = "서울 성북구 중앙로10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        pharmacySearchService.searchPharmacyDtoList() >> pharmacyDtoList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetPharmacyName == "호수온누리약국"
        results.get(1).targetPharmacyName == "돌곳이온누리약국"
    }

    def "buildDirectionList - 정해지 반경 10 KM 내에 검색이 되는지 확인"() {
        given:
        pharmacyDtoList.add(
                PharmacyDto.builder()
                .id(3L)
                .pharmacyName("경기약국")
                .pharmacyAddress("주소3")
                .latitude(37.3825107393401)
                .longitude(127.236707811313)
                .build()
        )

        def addressName = "서울 성북구 중앙로10길"
        double inputLatitude = 37.5960650456809
        double inputLongitude = 127.037033003036

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        pharmacySearchService.searchPharmacyDtoList() >> pharmacyDtoList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetPharmacyName == "호수온누리약국"
        results.get(1).targetPharmacyName == "돌곳이온누리약국"
    }
}
