package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//검색 결과 반환
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PinSearchResDTO {
    private Long pinId;
    private String title;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lng")
    private Double longitude;
    private Long reviewCount;
}
