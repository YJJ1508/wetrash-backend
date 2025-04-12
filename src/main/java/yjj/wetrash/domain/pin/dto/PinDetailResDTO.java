package yjj.wetrash.domain.pin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//상세 페이지 위한 DTO
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PinDetailResDTO {
    private String title;
    private String description;
    private List<String> trashTypes;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate createdAt;
    private List<PinDetailReviewResDTO> pinReviews;

    public static PinDetailResDTO fromEntity(Pin pin, List<PinDetailReviewResDTO> reviews){
        List<String> trashTypes = Stream.of(pin.getTrashcanType1(), pin.getTrashcanType2())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return PinDetailResDTO.builder()
                .title(pin.getTitle())
                .description(pin.getDescription())
                .trashTypes(trashTypes)
                .createdAt(pin.getCreatedAt().toLocalDate())
                .pinReviews(reviews)
                .build();
    }
}
