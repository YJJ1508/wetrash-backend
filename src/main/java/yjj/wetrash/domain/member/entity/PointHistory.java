package yjj.wetrash.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
import yjj.wetrash.domain.pin.entity.Pin;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PointHistory {
    public enum PointReason{
        REVIEW, PIN_APPROVED
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointReason pointReason;

    @ManyToOne
    @JoinColumn(name = "pin_id", nullable = true) //추후 핀 삭제될 수 있음
    private Pin pin; //어떤 pin에 관한건지 관리 차원에서 저장

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PointHistory(Member member, int point, PointReason pointReason, Pin pin){
        this.member = member;
        this.point = point;
        this.pointReason = pointReason;
        this.pin = pin;
    }

    public static PointHistory createForPinApproval(Member member, Pin pin){
        return PointHistory.builder()
                .member(member)
                .point(300)
                .pointReason(PointReason.PIN_APPROVED)
                .pin(pin)
                .build();
    }
    public static PointHistory createForReview(Member member, Pin pin){
        return PointHistory.builder()
                .member(member)
                .point(5)
                .pointReason(PointReason.REVIEW)
                .pin(pin)
                .build();
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void setPinToNull(){
        this.pin = null;
    }

}
