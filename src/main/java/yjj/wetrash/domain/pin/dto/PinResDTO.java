package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.Pin;

import java.util.Optional;

//핀 모두 조회  말풍선 용
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PinResDTO {
    private Long pinId;
    private String title;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    private double longitude;

    private String type;    //공유 url 시 말풍선 띄우는 목적 위함.

    public static PinResDTO fromEntity(Pin pin){
        String type = Optional.ofNullable(pin.getTrashcanType1())
                .orElse(pin.getTrashcanType2());

        return PinResDTO.builder()
                .pinId(pin.getId())
                .title(pin.getTitle())
                .latitude(pin.getLatitude())
                .longitude(pin.getLongitude())
                .type(type)
                .build();
    }


}
