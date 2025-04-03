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
    private int pinWarningCount; //관리자가

    //게시글 관련 신고
    private int boardReportCount; //사용자가

    //회원 총 경고 횟수 (지도+핀)
    private int totalWarnCount;                    //정지 회원이 됐을때 모든 warning 기록 초기화???


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
    public void addPinWarning(){
        pinWarningCount++;
    }
    public void evaluateAutoWarning(){ //핀 5회 누적 경고 시 ->  회원 상태 경고로 처리.
        if (pinWarningCount > 5){
            this.member.setStatusToWarning(); //경고
            this.pinWarningCount = 0; //초기화
            this.totalWarnCount++;
        }
    }
    public void evaluateAutoBan(){ //누적 경고 5회 이상 -> 회원 정지
        if (totalWarnCount > 5){
            this.member.setStatusToBan();
            this.totalWarnCount = 0;
        }
    }


}
