package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.global.security.jwt.entity.RefreshToken;
import yjj.wetrash.global.security.jwt.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    //refresh 토큰 저장
    @Transactional
    public void saveRefreshToken(String refreshToken, String memberId){
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .memberId(memberId)
                .build();
        refreshTokenRepository.save(token);
    }

    //refresh 토큰 삭제
    @Transactional
    public void deleteRefreshToken(String memberId){
        refreshTokenRepository.findByMemberId(memberId)
                .ifPresent(refreshTokenRepository::delete);
    }
}
