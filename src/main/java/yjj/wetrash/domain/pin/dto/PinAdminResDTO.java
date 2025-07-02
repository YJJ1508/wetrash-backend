package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.Pin;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
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
                .createdAt(pin.getCreatedAt().toLocalDate())
                .trashType1(pin.getTrashcanType1())
                .trashType2(pin.getTrashcanType2())
                .build();
    }
}
