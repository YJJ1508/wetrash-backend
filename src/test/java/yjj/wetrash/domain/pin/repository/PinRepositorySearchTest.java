package yjj.wetrash.domain.pin.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import yjj.wetrash.WetrashBackendApplication;
import yjj.wetrash.domain.pin.dto.PinSearchResDTO;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;
import yjj.wetrash.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
@ContextConfiguration(classes = WetrashBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinRepositorySearchTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private EntityManager em;

    //1.제목 2.설명 3.제목 포함이 설명보다 먼저 나오기(필요한가?)
    /* 조건 분기
      -) 1.거리순  (디폴트)
      -)   거리 디폴트 확인.
      -) 2.리뷰순
      -) 3.즐겨찾기 순
    */

    @BeforeEach
    void setup(){ //테스트용 데이터 저장
        Pin pin1 = Pin.builder().title("설명에 park 포함").description("공원이요").lat(37.1).lng(127.1).build();
        Pin pin2 = Pin.builder().title("공원 요정").lat(37.0).lng(127.0).build();
        Pin pin3 = Pin.builder().title("포함 x").lat(37.0).lng(127.0).build();
        Pin pin4 = Pin.builder().title("필터링 조건 확인용").description("공원").lat(37.0).lng(127.0).trashcanType1("일반쓰레기").build();
        em.persist(pin1);
        em.persist(pin2);
        em.persist(pin3);
        em.persist(pin4);
        // 리뷰순 위한 테스트
        PinReview pinReview = PinReview.builder().pin(pin1).comment("리뷰 테스트 용도").build();
        em.persist(pinReview);
    }

    @Test
    @DisplayName("핀 검색 테스트 (키워드와 조건분기)")
    void searchPinsTest(){
        //given
        String keyword = "공원";
        Double lat = 37.0;
        Double lng = 127.0;
        String sortType = "리뷰순";
        String trashType = "일반쓰레기";
        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<PinSearchResDTO> results = pinRepository.searchPins(keyword, lat, lng, sortType, trashType, pageable);
        //then
//        Assertions.assertThat(results.getTotalElements()).isEqualTo(4); //제목,설명 포함 확인 (0)
//        Assertions.assertThat(results.getContent().get((3)).getTitle()).contains("설명에");//제목 우선 포함 확인 (0)
//        Assertions.assertThat(results.getContent().get(0).getTitle()).contains("요정");// 거리순 확인 (0)
//        Assertions.assertThat(results.getContent().get(0).getPinId()).isEqualTo(1); //인기순 (즐겨찾기)
        //Assertions.assertThat(results.getContent().get(0).getPinId()).isEqualTo(1); //리뷰순
        //Assertions.assertThat(results.getContent().get(1).getTitle()).contains("설명");
        Assertions.assertThat(results.getTotalElements()).isEqualTo(3); //필터링 테스트
    }

    @Test
    @DisplayName("핀 검색 테스트 (키워드와 조건분기 null일때)")
    void searchPinsTest2(){
        //given
        String keyword = "설명";
        Double lat = 37.0;
        Double lng = 127.0;
        String sortType = null;
        String trashType = null;
        Pageable pageable = PageRequest.of(0,10);
        //when
        Page<PinSearchResDTO> results = pinRepository.searchPins(keyword, lat, lng, sortType, trashType, pageable);
        //then
        Assertions.assertThat(results.getTotalElements()).isEqualTo(1); //필터링 테스트
    }

}
