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


    //회원 경고 관리
    private int warningCount; //admin 수동 경고 ++ (5회 이상 시 경고 처리)
    private int totalWarnCount;  //warningCount 5회 이상 -> totalWarnCount 1++ (5회 시
    private static final int WARNING_THRESHOLD = 5;
    private static final int BAN_THRESHOLD = 3;


    public MemberReputation(Member member){
        this.member = member;
    }

    //지도 핀 관련
    public void approval(){
        totalPinRequests++;
        approvedPins++;
    }
    public void rejection(){
        totalPinRequests++;
        rejectedPins++;
    }
    public void addAdminWarning(){
        warningCount++;
        evaluateAutoWarning();
        evaluateAutoBan();
    }
    public void evaluateAutoWarning(){ //핀 5회 누적 경고 시 ->  회원 상태 경고로 처리.
        if (warningCount >= WARNING_THRESHOLD){
            this.member.setStatusToWarning(); //경고
            this.warningCount = 0; //초기화
            this.totalWarnCount++;
        }
    }
    public void evaluateAutoBan(){ //누적 경고 3회 이상 -> 회원 정지
        if (totalWarnCount >= BAN_THRESHOLD){
            this.member.setStatusToBan();
            this.warningCount = 0;
            this.totalWarnCount = 0;
        }
    }


}
