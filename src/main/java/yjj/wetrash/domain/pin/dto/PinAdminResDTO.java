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
public class PinAdminResDTO {  //관리자 페이지로 전송

    private Long id;
    private double latitude;
    private double longitude;
    private String title;
    private String description;
    private String requesterEmail;
    private String requesterNickname;
    private String trashType1;  //일반 쓰레기
    private String trashType2;

    public static PinAdminResDTO fromEntity(Pin pin){
        return PinAdminResDTO.builder()
                .id(pin.getId())
                .latitude(pin.getLatitude())
                .longitude(pin.getLongitude())
                .title(pin.getTitle())
                .description(pin.getDescription())
                .requesterEmail(pin.getRequestMember().getEmail())
                .requesterNickname(pin.getRequestMember().getNickname())
                .trashType1(pin.getTrashcanType1())
                .trashType2(pin.getTrashcanType2())
                .build();
    }
}
