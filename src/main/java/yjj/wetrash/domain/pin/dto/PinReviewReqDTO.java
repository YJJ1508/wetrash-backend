package yjj.wetrash.domain.pin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;

@Getter
@NoArgsConstructor
public class PinReviewReqDTO {
    private String comment;
    private Long pinId;

    public PinReview toEntity(Member member, Pin pin){
        return PinReview.builder()
                .member(member)
                .pin(pin)
                .comment(this.comment)
                .build();
    }
}
