package yjj.wetrash.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinFavorite;

import java.util.Optional;

public interface PinFavoriteRepository extends JpaRepository<PinFavorite, Long> {

    Optional<PinFavorite> findByMemberAndPin(Member member, Pin pin);

    boolean existsPinFavoriteByMemberAndPin(Member member, Pin pin);

}
