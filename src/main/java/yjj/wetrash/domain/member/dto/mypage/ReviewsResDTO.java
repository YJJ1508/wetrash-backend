package yjj.wetrash.domain.member.dto.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.PinReview;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewsResDTO {
    private Long reviewId;
    private Long pinId;
    private String title; //pin location
    private String comment;
    private String nickname; //리뷰 작성자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private int count; //핀 리뷰 수

    public static ReviewsResDTO from(PinReview pinReview){
        return ReviewsResDTO.builder()
                .reviewId(pinReview.getId())
                .pinId(pinReview.getPin().getId())
                .title(pinReview.getPin().getTitle())
                .comment(pinReview.getComment())
                .nickname(pinReview.getMember().getNickname())
                .createdAt(pinReview.getCreatedAt())
                .count(pinReview.getPin().getPinReviews().size())
                .build();
    }

}
