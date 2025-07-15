package com.keotam.cafe.service;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;

    public List<CafeSearchResponse> searchCafes(CafeSearchRequest searchRequest){
        int offset = searchRequest.getPage() * searchRequest.getSize();
        List<CafeSearchResponse> cafes = cafeRepository.findNearbyCafes(
                searchRequest.getLongitude(),
                searchRequest.getLatitude(),
                searchRequest.getSize(),
                offset,
                searchRequest.getKeyword()
        );

        return cafes;
        //TODO 사용자 위치 - 카페 간 거리 km 단위로 찾아서 조회하는 부분 필요
        //이걸 할때는 havor를 쓸 가 싶긴함
    }
}
