package com.keotam.cafe.service;

import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeDetailResponse;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import com.keotam.cafe.dto.response.MenuResponse;
import com.keotam.cafe.exception.CafeDetailNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    }

    public List<CafeDetailResponse> getCafeDetail(Long cafeId){
        Cafe cafe = cafeRepository.findByIdWithBrandAndMenus(cafeId)
                .orElseThrow(CafeDetailNotFound::new);

        List<BrandMenu> menus = cafe.getBrand().getMenus();
        Map<String, List<MenuResponse>> menuResponse = menus.stream()
                .map(MenuResponse::fromEntity)
                .sorted(Comparator.comparing(MenuResponse::getPrice))
                .collect(Collectors.groupingBy(MenuResponse::getCategory));
        return menuResponse.entrySet()
                .stream()
                .map(entry -> new CafeDetailResponse(entry.getKey(),entry.getValue()))
                .toList();
    }
}
