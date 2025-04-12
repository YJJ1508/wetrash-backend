package yjj.wetrash.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;

import java.util.List;
import java.util.Optional;

public interface PinReviewRepository extends JpaRepository<PinReview, Long> {
    List<PinReview> findAllByPin(Pin pin);
}
