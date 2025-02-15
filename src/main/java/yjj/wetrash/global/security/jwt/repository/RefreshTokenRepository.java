package yjj.wetrash.global.security.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import yjj.wetrash.global.security.jwt.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByMemberId(String MemberId);
}
