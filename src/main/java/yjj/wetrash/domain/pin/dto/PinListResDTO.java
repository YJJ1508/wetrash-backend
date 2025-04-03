package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import yjj.wetrash.domain.pin.entity.Pin;
//핀 모두 조회
@Builder
@Getter
public class PinListResDTO {
    private Long pinId;
    private String title;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;

    public static PinListResDTO fromEntity(Pin pin){
        return PinListResDTO.builder()
                .pinId(pin.getId())
                .title(pin.getTitle())
                .latitude(pin.getLatitude())
                .longitude(pin.getLongitude())
                .build();
    }
}
