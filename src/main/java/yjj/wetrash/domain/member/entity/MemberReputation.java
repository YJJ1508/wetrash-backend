package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
// PIN + 게시글 통합 신고 처리 (사용자 관리)
@Entity
public class MemberReputation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    //지도 핀 관련 정지
    private int totalPinRequests;
    private int approvedPins;
    private int rejectedPins;
    private int PinReportCount;

    //게시글 관련 신고
    private int boardReportCount;

}
