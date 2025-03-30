package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// PIN + 게시글 통합 신고 처리 (사용자 관리)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReputation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    //지도 핀 관련 정지
    private int totalPinRequests;
    private int approvedPins;
    private int rejectedPins;

    //게시글 관련 신고
    private int boardReportCount;

    public MemberReputation(Member member){
        this.member = member;
    }

    public void approval(){
        totalPinRequests++;
        approvedPins++;
    }
    public void rejection(){
        totalPinRequests++;
        rejectedPins++;
    }


}
