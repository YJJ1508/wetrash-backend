package yjj.wetrash.domain.pin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;
// 사용자 핀 요청
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinRequestDTO {

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String title;
    @NotNull
    private String description;

    private String trashType1;  //일반 쓰레기
    private String trashType2;  //재활용 쓰레기

    public Pin toEntity(Member member){
        return Pin.builder()
                .lat(latitude)
                .lng(longitude)
                .title(title)
                .description(description)
                .trashcanType1(trashType1)
                .trashcanType2(trashType2)
                .status(PinStatus.PENDING) //초기 보류 상태
                .requestMember(member)
                .build();
    }

}
