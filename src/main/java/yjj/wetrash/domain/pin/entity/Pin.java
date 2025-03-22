package yjj.wetrash.domain.pin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Pin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private String title;
    private String description;
    @Column(name = "trashcan_type")
    private String trashcanType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member requestMember; //핀 요청 유저

    @Enumerated(EnumType.STRING)
    private PinStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Builder
    public Pin(double lat, double lng, String title, String description, String trashcanType,
               Member requestMember, PinStatus status){
        this.latitude = lat;
        this.longitude = lng;
        this.title = title;
        this.description = description;
        this.trashcanType = trashcanType;
        this.requestMember = requestMember;
        this.status = status;
    }

}
