package com.keotam.cafe.presentation;

import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import com.keotam.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/cafe") // ✅ 여기에 base path 설정
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @GetMapping
    public ResponseEntity<List<CafeSearchResponse>> getCafes(CafeSearchRequest request) {
        List<CafeSearchResponse> cafeSearchResponses = cafeService.searchCafes(request);

        return ResponseEntity.ok(cafeSearchResponses);
    }
}
