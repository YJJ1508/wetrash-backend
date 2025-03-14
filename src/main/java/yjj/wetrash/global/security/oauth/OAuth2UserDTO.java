package yjj.wetrash.global.security.oauth;

import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.Role;
import yjj.wetrash.global.exception.CustomException;

import java.util.Map;
@Builder
@Slf4j
public record OAuth2UserDTO(
        String email, //record 로 private 선언됨
        String profile,
        String nickname,
        String provider
) {
    public static OAuth2UserDTO of(String providerId, Map<String, Object> attributes, String userNameAttributeName){
        return switch (providerId){
            case "google" -> ofGoogle(attributes, userNameAttributeName);
            case "naver" -> ofNaver(attributes, userNameAttributeName);
            case "kakao" -> ofKakao(attributes, userNameAttributeName);
            default -> throw new CustomException(OAuth2ErrorCode.ILLEGAL_PROVIDER_ID);
        };
    }

    private static OAuth2UserDTO ofGoogle(Map<String, Object> attributes, String userNameAttributeName){
        return OAuth2UserDTO.builder()
                .email(attributes.get("email").toString())
                .profile(attributes.get("picture").toString())
                .nickname(attributes.get(userNameAttributeName).toString())
                .provider("google")
                .build();
    }
    private static OAuth2UserDTO ofNaver(Map<String, Object> attributes, String userNameAttributeName){
        Map<String, Object> response = (Map<String, Object>) attributes.get(userNameAttributeName);
        return OAuth2UserDTO.builder()
                .email(response.get("email").toString())
                .profile(response.get("profile_image").toString())
                .nickname(response.get("id").toString())
                .provider("naver")
                .build();
    }
    private static OAuth2UserDTO ofKakao(Map<String, Object> attributes, String userNameAttributeName){
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserDTO.builder()
                .email(account.get("email").toString())
                .profile(profile.get("profile_image_url").toString())
                .nickname(attributes.get(userNameAttributeName).toString())
                .provider("kakao")
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .nickname(nickname) //고유값
                .profile(profile)
                .provider(provider)
                .role(Role.USER)
                .build();
    }
}
