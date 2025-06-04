package yjj.wetrash.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.Member;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointRankingResDTO {
    private String nickname;
    private int totalPoint;
    private int rank;

    public static PointRankingResDTO from(Member member, int rank){ //top10
        return PointRankingResDTO.builder()
                .nickname(member.getNickname())
                .totalPoint(member.getTotalPoint())
                .rank(rank)
                .build();
    }

    public static PointRankingResDTO fromRaw(Object[] row, int rank){ //지난달 top10
        return PointRankingResDTO.builder()
                .nickname((String) row[1])
                .totalPoint(((Number) row[2]).intValue())
                .rank(rank)
                .build();
    }
}
