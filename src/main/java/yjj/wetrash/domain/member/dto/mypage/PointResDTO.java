package yjj.wetrash.domain.member.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.PointHistory;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointResDTO {
    private int totalPoint;
    private String tier;
}
