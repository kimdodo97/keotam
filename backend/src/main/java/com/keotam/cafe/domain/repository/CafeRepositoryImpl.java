package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Cafe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import static com.keotam.cafe.domain.QBrand.*;
import static com.keotam.cafe.domain.QBrandMenu.*;
import static com.keotam.cafe.domain.QCafe.*;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Cafe> findByIdWithBrandAndMenus(Long cafeId) {
        return Optional.ofNullable(queryFactory.selectFrom(cafe)
                .join(cafe.brand, brand).fetchJoin()
                .join(brand.menus, brandMenu).fetchJoin()
                .where(cafe.id.eq(cafeId))
                .distinct()
                .fetchOne());
    }
}
