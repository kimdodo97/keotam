package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> , CafeRepositoryCustom{
    @Query(value = """
        SELECT
            cafe_id as cafeId,
            name,
            address,
            ST_Distance(
                location,
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography
            ) AS distance
        FROM app.cafe
        WHERE (:keyword IS NULL OR name ILIKE '%' || :keyword || '%')
        ORDER BY distance ASC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<CafeSearchResponse> findNearbyCafes(
            @Param("lon") double longitude,
            @Param("lat") double latitude,
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("keyword") String keyword
    );
}
