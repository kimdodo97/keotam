package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.QCafe;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Cafe> searchCafes(CafeSearchRequest cafeSearchRequest) {
        QCafe cafe = QCafe.cafe;
        BooleanBuilder builder = new BooleanBuilder();

        if(cafeSearchRequest.getKeyword() != null && !cafeSearchRequest.getKeyword().isEmpty()) {
            builder.and(cafe.name.containsIgnoreCase(cafeSearchRequest.getKeyword()));
        }

        Pageable pageable = PageRequest.of(cafeSearchRequest.getPage(), cafeSearchRequest.getSize());

        return queryFactory.selectFrom(cafe)
                .where(builder)
                .orderBy(
                        Expressions.numberTemplate(Double.class,
                                "ST_DistanceSphere(" +
                                        "ST_MakePoint(cast({0} as double precision), cast({1} as double precision)), " +
                                        "ST_MakePoint(cast({2} as double precision), cast({3} as double precision))" +
                                        ")",
                                cafeSearchRequest.getLongitude(), cafeSearchRequest.getLatitude(),
                                cafe.longitude, cafe.latitude
                        ).asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
