package yjj.wetrash.domain.pin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yjj.wetrash.domain.pin.dto.PinSearchResDTO;
import yjj.wetrash.domain.pin.entity.Pin;

import java.util.List;

public interface PinCustomRepository {
    Page<PinSearchResDTO> searchPins(String keyword, Double userLat, Double userLng,
                                     String sortType, String trashType, Pageable pageable);
}
