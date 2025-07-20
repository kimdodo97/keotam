package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.BrandMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandMenuRepository extends JpaRepository<BrandMenu, Long> {
}
