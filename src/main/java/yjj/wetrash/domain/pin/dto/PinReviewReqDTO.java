package yjj.wetrash.domain.pin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;

@Getter
@NoArgsConstructor
@Builder //for test
@AllArgsConstructor
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
