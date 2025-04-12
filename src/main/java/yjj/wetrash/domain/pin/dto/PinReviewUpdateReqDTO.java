package yjj.wetrash.domain.pin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.Pin;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinReviewUpdateReqDTO {
    private String comment;
}
