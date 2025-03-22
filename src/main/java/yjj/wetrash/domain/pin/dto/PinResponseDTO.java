package yjj.wetrash.domain.pin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.Pin;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PinResponseDTO {

    private double latitude;
    private double longitude;
    private String title;
    private String description;
    private String requesterEmail;
    private String requesterNickname;

    public static PinResponseDTO fromEntity(Pin pin){
        return PinResponseDTO.builder()
                .latitude(pin.getLatitude())
                .longitude(pin.getLongitude())
                .title(pin.getTitle())
                .description(pin.getDescription())
                .requesterEmail(pin.getRequestMember().getEmail())
                .requesterNickname(pin.getRequestMember().getNickname())
                .build();
    }
}
