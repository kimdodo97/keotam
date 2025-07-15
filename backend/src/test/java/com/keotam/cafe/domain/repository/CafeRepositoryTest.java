package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import com.keotam.cafe.utils.PointUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CafeRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgis = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.5-alpine").asCompatibleSubstituteFor("postgres")
    )
            .withDatabaseName("testdb")
            .withUsername("postgis")
            .withPassword("1234")
            .withInitScript("init.sql");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgis::getJdbcUrl);
        registry.add("spring.datasource.username", postgis::getUsername);
        registry.add("spring.datasource.password", postgis::getPassword);
        registry.add("spring.datasource.driver-class-name", postgis::getDriverClassName);
    }

    @BeforeAll
    public static void setUp() {
        postgis.start();
    }

    @Autowired
    private CafeRepository cafeRepository;

    @Test
    @DisplayName("카페 조회 시 검색어가 없으면 가까운 기준으로 페이지 수만큼 조회한다.")
    void searchCafesByDistance() {
        // given: 두 개의 카페를 저장
        cafeRepository.save(Cafe.builder()
                .name("카페A")
                .address("서울")
                .latitude(37.5665)
                .longitude(126.9780)
                .location(PointUtil.createPoint(37.5665, 126.9780))
                .build());
        cafeRepository.save(Cafe.builder()
                .name("카페B")   // 중간에 추가
                .address("대전")
                .latitude(36.3504)
                .longitude(127.3845)
                .location(PointUtil.createPoint(36.3504, 127.3845))
                .build());
        cafeRepository.save(Cafe.builder()
                .name("카페C")
                .address("부산")
                .latitude(35.1796)
                .longitude(129.0756)
                .location(PointUtil.createPoint(35.1796, 129.0756))
                .build());

        // when: 서울 기준으로 거리 정렬
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(35.1796)
                .longitude(129.0756)
                .page(0)
                .size(10)
                .keyword("")  // 필터링 없이 전체
                .build();

        int pageSize = request.getSize();
        int offset = request.getPage() * pageSize;
        List<CafeSearchResponse> result = cafeRepository.findNearbyCafes(request.getLongitude(),request.getLatitude()
                ,pageSize,offset,request.getKeyword());

        // then: 카페A가 먼저 나와야 함
        assertEquals(3,result.size());
        assertEquals(result.get(0).getName(),"카페C");
        assertNotNull(result.get(0).getDistance());
    }

    @Test
    @DisplayName("카페 조회 시 검색어가 있으면 검색어를 포함한 카페를 거리가 가까운 순으로 조회한다.")
    void findByCafeWithSearchWord() throws Exception {
        //given
        List<Cafe> cafes =
                java.util.stream.IntStream.rangeClosed(1, 20)
                        .mapToObj(i -> Cafe.builder()
                                .name("카페 " + i)
                                .address("위치 "+ i)
                                .longitude((double)i)
                                .latitude((double)i*10)
                                .location(PointUtil.createPoint(i, i*10))
                                .build()
                        ).toList();

        cafeRepository.saveAll(cafes);
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(1.0)
                .longitude(10.0)
                .page(0)
                .size(10)
                .keyword("5")  // 필터링 없이 전체
                .build();
        //when
        int pageSize = request.getSize();
        int offset = request.getPage() * pageSize;
        List<CafeSearchResponse> result = cafeRepository.findNearbyCafes(request.getLongitude(),request.getLatitude()
                ,pageSize,offset,request.getKeyword());
        //then
        assertEquals(2,result.size());
        assertEquals(result.get(0).getName(),"카페 5");
    }
}