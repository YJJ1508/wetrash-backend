package yjj.wetrash.domain.pin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import yjj.wetrash.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
