package yjj.wetrash.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUploadResDTO {
    private String profileUrl;

    public static ProfileUploadResDTO of(String file){
        return ProfileUploadResDTO.builder()
                .profileUrl(file)
                .build();
    }
}
