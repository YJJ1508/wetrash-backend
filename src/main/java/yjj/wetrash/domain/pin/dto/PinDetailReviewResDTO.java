package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.PinReview;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PinDetailReviewResDTO {
    private Long reviewId;
    private String nickname;
    private Long memberId;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime displayTime;
    private boolean edited;

    public static PinDetailReviewResDTO fromEntity(PinReview pinReview){
        boolean isEdited = !pinReview.getCreatedAt().isEqual(pinReview.getUpdatedAt());

        return PinDetailReviewResDTO.builder()
                .reviewId(pinReview.getId())
                .nickname(pinReview.getMember().getNickname())
                .memberId(pinReview.getMember().getId())
                .comment(pinReview.getComment())
                .displayTime(isEdited ? pinReview.getUpdatedAt() : pinReview.getCreatedAt())
                .edited(isEdited)
                .build();
    }
}
