package yjj.wetrash.domain.pin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "pin_favorite",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_member_id", "pin_pin_id"})
        })
public class PinFavorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pinFavorite_id")
    private Long id;

    @ManyToOne
    private Pin pin;

    @ManyToOne
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;

}
