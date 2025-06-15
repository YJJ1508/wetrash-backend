package yjj.wetrash.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.entity.Role;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.security.CustomDetails;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000*60*30; //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000*60*24*7; //7일 1000*60*24*7
    private static final long THREE_DAYS = 1000 * 60 * 60 * 24 * 3;  // 3일
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private final Key key; //HMAC 알고리즘 사용 위한 Key 객체

    private final MemberRepository memberRepository;


    /*
        토큰 생성
    */
    //access token 생성
    public String createAccessToken(Authentication authentication){
        //권한 가져오기
//        String authority = authentication.getAuthorities().stream()
//                .findFirst()
//                .map(GrantedAuthority::getAuthority)
//                .orElse("ROLE_USER");
        CustomDetails principal = (CustomDetails) authentication.getPrincipal();
        String role = principal.getRole().name();

        Date now = new Date();
        Date expiredDate = new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    //refresh token 생성
    public String createRefreshToken(Authentication authentication){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime()+REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // 토큰 생성시: access 만료 시간 추출
    public Long getAccessTokenExpiration(String accessToken){
        Claims claims = parseClaims(accessToken);
        return claims.getExpiration().getTime();
    }

    /*
        토큰에서 회원 정보 추출
     */
    public Authentication getAuthentication(String accessToken){
        //토큰 복호화
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY)==null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        //권한 정보 추출
        String authority = claims.get(AUTHORITIES_KEY).toString(); //단일 권한
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(authority));
        String email = (String) claims.getSubject();
//        String roleStr = (String) claims.get("auth", String.class); 회원 db 조회로 변경
//        Role role = Role.valueOf(roleStr); //Role.USER (USER)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        CustomDetails principal = new CustomDetails(member);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    /*
        토큰 검증
     */
    //토큰에서 claim 해석(parsing) & 검증 객체 생성
    public Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); //claim 추출
        }catch (ExpiredJwtException e){
            return e.getClaims();   //만료된 토큰도 반환
        }
    }
    //토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    public boolean refreshTokenPeriodCheck(String token) {
        Claims claims = parseClaims(token);
        long now = new Date().getTime();
        long refresh_expiredTime = claims.getExpiration().getTime(); //7일후
        long refresh_nowTime = now + REFRESH_TOKEN_EXPIRE_TIME;     //현재 + 7일후

        if (refresh_nowTime - refresh_expiredTime > THREE_DAYS) {
            return true;
        }
        return false;
    }


}
