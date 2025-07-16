package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.dto.request.CafeSearchRequest;

import java.util.List;
import java.util.Optional;

public interface CafeRepositoryCustom {
    public Optional<Cafe> findByIdWithBrandAndMenus(Long cafeId);
}
