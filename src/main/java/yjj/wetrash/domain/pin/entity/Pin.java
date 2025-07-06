package yjj.wetrash.domain.pin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.dto.PinAdminUpdateReqDTO;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Pin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "trashcan_type1")
    private String trashcanType1;   //일반쓰레기
    @Column(name = "trashcan_type2")
    private String trashcanType2;   //재활용쓰레기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member requestMember; //핀 요청 유저

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PinStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "pin")
    private List<PinReview> pinReviews;

    @Builder
    public Pin(double lat, double lng, String title, String description, String trashcanType1, String trashcanType2,
               Member requestMember, PinStatus status){
        this.latitude = lat;
        this.longitude = lng;
        this.title = title;
        this.description = description;
        this.trashcanType1 = trashcanType1;
        this.trashcanType2 = trashcanType2;
        this.requestMember = requestMember;
        this.status = status;
    }

    public void updateOnApprove(String title, String description){ //관리자 승인
        this.title = title;
        this.description = description;
        this.status = PinStatus.APPROVED;
    }

    public void updateOnReject(){
        this.status = PinStatus.REJECTED;
    }
    public void updateOnApproved(){
        this.status = PinStatus.APPROVED;
    }
    public void updateOnRemoved(){
        this.status = PinStatus.REMOVED;
    }

    public void updateApprovedPinByAdmin(PinAdminUpdateReqDTO dto){
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.trashcanType1 = dto.getTrashType1();
        this.trashcanType2 = dto.getTrashType2();
    }

}
