package com.keotam.cafe.service;

import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CafeServiceTest {
    @Mock
    private CafeRepository cafeRepository;

    @InjectMocks
    private CafeService cafeService;

    @Test
    @DisplayName("첫페이지 조회 요청 시 현재 위치 기준 가까운 카페가 정렬되어 조회된다.")
    void findByDefaultSearch() throws Exception {
        //given
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(37.5665)
                .longitude(126.9780)
                .keyword("")
                .page(1)
                .size(10)
                .build();

        List<CafeSearchResponse> mockResults =
                java.util.stream.IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new CafeSearchResponse(
                                (long) i,
                                "카페 " + i,
                                "서울시 어딘가",
                                i * 10.0  // 거리: 10.0, 20.0, ..., 200.0
                        ))
                        .toList();

        int offset = request.getPage() * request.getSize();
        given(cafeRepository.findNearbyCafes(
                request.getLongitude(),
                request.getLatitude(),
                request.getSize(),
                offset,
                request.getKeyword()
        )).willReturn(mockResults);
        //when
        List<CafeSearchResponse> result = cafeService.searchCafes(request);
        //then
        assertEquals(10, result.size());
        assertEquals(result.get(0).getName(),"카페 1");
        assertEquals(result.get(9).getDistance(),100.0);
    }

    @Test
    @DisplayName("카페 목록 조회 요청 시 검색어가 있는 경우 검색를 포함한 현재 위치 기준 가까운 카페가 정렬되어 조회된다.")
    void findWithSearchWord() throws Exception {
        //given
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(37.5665)
                .longitude(126.9780)
                .keyword("5")
                .page(1)
                .size(10)
                .build();

        List<CafeSearchResponse> mockResults =
                java.util.stream.IntStream.rangeClosed(5,5)
                        .mapToObj(i -> new CafeSearchResponse(
                                (long) i,
                                "카페 " + i,
                                "서울시 어딘가",
                                i * 50.0  // 거리: 10.0, 20.0, ..., 200.0
                        ))
                        .toList();

        int offset = request.getPage() * request.getSize();
        given(cafeRepository.findNearbyCafes(
                request.getLongitude(),
                request.getLatitude(),
                request.getSize(),
                offset,
                request.getKeyword()
        )).willReturn(mockResults);
        //when
        List<CafeSearchResponse> result = cafeService.searchCafes(request);
        //then
        assertEquals(1, result.size());
        assertEquals(result.get(0).getName(),"카페 5");
        assertEquals(result.get(0).getDistance(),250.0);
    }
}