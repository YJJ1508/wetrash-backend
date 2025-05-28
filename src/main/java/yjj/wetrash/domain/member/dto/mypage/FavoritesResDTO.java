package yjj.wetrash.domain.member.dto.mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinFavorite;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritesResDTO {
    private Long favoriteId;
    private Long pinId;
    private String title; //pin location(title)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; //즐겨찾기 등록일

    public static FavoritesResDTO from(PinFavorite pinFavorite){
        return FavoritesResDTO.builder()
                .favoriteId(pinFavorite.getId())
                .pinId(pinFavorite.getPin().getId())
                .title(pinFavorite.getPin().getTitle())
                .description(pinFavorite.getPin().getDescription())
                .createdAt(pinFavorite.getCreatedAt())
                .build();
    }
}
