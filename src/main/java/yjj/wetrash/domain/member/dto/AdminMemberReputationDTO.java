package yjj.wetrash.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.member.entity.MemberReputation;

//관리자용 사용자 memberReputation 조회
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberReputationDTO {

    private String email;
    private String nickname;
    private int totalPinRequests;
    private int approvedPins;
    private int rejectedPins;
    //게시글 관련 신고
    private int boardReportCount;

    public static AdminMemberReputationDTO fromEntity(MemberReputation memberReputation){
        return AdminMemberReputationDTO.builder()
                .email(memberReputation.getMember().getEmail())
                .nickname(memberReputation.getMember().getNickname())
                .totalPinRequests(memberReputation.getTotalPinRequests())
                .approvedPins(memberReputation.getApprovedPins())
                .rejectedPins(memberReputation.getRejectedPins())
                .build();
    }
}
