package yjj.wetrash.domain.pin.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import yjj.wetrash.domain.pin.dto.PinSearchResDTO;

@SpringBootTest
@DisplayName("PinService 검색 테스트")
public class PinServiceTest {

    @Autowired
    private PinService pinService;

    @Test
    @DisplayName("검색 서비스층에서 호출 테스트")
    void getSearchPins(){
        // given
        String keyword = "공원";
        Double lat = 37.0;
        Double lng = 127.0;
        String sortType = null;
        String trashType = null;
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<PinSearchResDTO> results = pinService.getSearchPins(keyword, lat, lng, sortType, trashType, pageable);
        // then
        Assertions.assertThat(results.getTotalElements()).isEqualTo(2);
    }

}
