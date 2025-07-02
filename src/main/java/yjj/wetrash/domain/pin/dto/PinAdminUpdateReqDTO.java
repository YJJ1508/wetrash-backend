package yjj.wetrash.domain.pin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinAdminUpdateReqDTO {
    private Long id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String trashType1;
    private String trashType2;
}
