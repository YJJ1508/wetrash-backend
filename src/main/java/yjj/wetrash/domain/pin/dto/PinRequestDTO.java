package yjj.wetrash.domain.pin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;

@Getter
public class PinRequestDTO {
    @NotBlank
    private Double latitude;
    @NotBlank
    private Double longitude;
    @NotBlank
    private String title;
    @NotNull
    private String description;
    @NotBlank
    private String trashType;

    public Pin toEntity(Member member){
        return Pin.builder()
                .lat(latitude)
                .lng(longitude)
                .title(title)
                .description(description)
                .trashcanType(trashType)
                .status(PinStatus.PENDING) //초기 보류 상태
                .requestMember(member)
                .build();
    }

}
