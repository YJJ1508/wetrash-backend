package yjj.wetrash.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    public List<Pin> findAllByStatus(PinStatus status);

}
