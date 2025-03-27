package yjj.wetrash.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;

import java.util.List;
import java.util.Optional;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByStatus(PinStatus status);
    Long countPinByStatus(PinStatus status);
    Optional<Pin> findPinById(Long id);
}
