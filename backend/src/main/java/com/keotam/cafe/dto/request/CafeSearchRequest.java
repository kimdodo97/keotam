package com.keotam.cafe.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Builder
@Getter
public class CafeSearchRequest {
    private String keyword;
    private Double latitude;
    private Double longitude;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String sortBy = "distance";

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.ASC;
}
