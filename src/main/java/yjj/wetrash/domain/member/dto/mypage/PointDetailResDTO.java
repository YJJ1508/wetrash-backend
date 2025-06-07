package yjj.wetrash.domain.member.dto.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.entity.PointHistory.PointReason;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDetailResDTO {
    private Long pointId;
    private int point;
    @Enumerated(value = EnumType.STRING)
    private PointReason pointReason;
    private String pin_title; //포인트 얻은 핀의 제목
    private Long pin_id; //포인트 얻은 핀의 id
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static PointDetailResDTO from(PointHistory pointHistory){
        return PointDetailResDTO.builder()
                .pointId(pointHistory.getId())
                .point(pointHistory.getPoint())
                .pointReason(pointHistory.getPointReason())
                .pin_id(pointHistory.getPin().getId())
                .pin_title(pointHistory.getPin().getTitle())
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}
