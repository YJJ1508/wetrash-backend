package yjj.wetrash.domain.member.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.dto.*;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberReputation;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.entity.Role;
import yjj.wetrash.domain.member.repository.MemberReputationRepository;
import yjj.wetrash.global.security.jwt.CustomUserDetailsService;
import yjj.wetrash.global.security.jwt.entity.RefreshToken;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.security.jwt.repository.RefreshTokenRepository;
import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;
import yjj.wetrash.global.security.jwt.dto.JwtTokenReqDTO;
import yjj.wetrash.global.security.util.CookieUtil;

import java.util.List;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberReputationRepository memberReputationRepository;

    //회원가입
    @Transactional
    public void signUp(SignUpReqDTO signUpDTO){
        //회원 중복 검사
        if (memberRepository.existsByEmail(signUpDTO.getEmail())){
            throw new CustomException(MemberErrorCode.USER_ALREADY_EXISTS);
        }
        //비번 암호화
        String encP = bCryptPasswordEncoder.encode(signUpDTO.getPassword());
        //회원 save
        Member member = memberRepository.save(signUpDTO.toEntity(encP));
        //회원 평판 생성 및 저장 (부가 정보)
        memberReputationRepository.save(member.createReputation());
    }

    //로그인 access + refresh 발급
    @Transactional
    public JwtTokenDTO signIn(LoginReqDTO loginReqDTO){
        //사용자 이메일,비번 담는 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginReqDTO.getEmail(), loginReqDTO.getPassword());
        //사용자 pw 검증 -> Authentication 객체 반환. (매니저가 authenticate() -> CustomUserDetailsService 메서드 호출)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //인증 정보를 기반으로 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        Long expiration = jwtTokenProvider.getAccessTokenExpiration(accessToken); //access 만료 시간
        JwtTokenDTO jwtTokenDTO = JwtTokenDTO.of("both", accessToken,refreshToken, expiration);
        //refresh 토큰 redis 저장
        refreshTokenService.saveRefreshToken(refreshToken, loginReqDTO.getEmail());
        return jwtTokenDTO;
    }

    //재발급: refresh 토큰 받아서 확인한 후 access 토큰 발급
    @Transactional
    public JwtTokenDTO reissue(String refreshToken){  //refreshToken 수령
        // 1.refresh token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)){
            throw new CustomException(MemberErrorCode.INVALID_REFRESH_TOKEN);
        }
        // 2.email 가져오기
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        //db(redis)에서 refresh 값 가져오기
        RefreshToken redisRefreshToken = refreshTokenRepository.findByMemberId(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.REFRESH_TOKEN_NOT_FOUND));
        // 3.Refresh Token 일치 하는지 검사
        if (!redisRefreshToken.getRefreshToken().equals(refreshToken)){
            throw new CustomException(MemberErrorCode.INVALID_REFRESH_TOKEN);
        }
        // Authentication 객체 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 4.토큰 재발급
        JwtTokenDTO tokenDTO = null;
        //refresh token의 유효기간이 3일 미만일 경우: access + refresh 재발급
        if(jwtTokenProvider.refreshTokenPeriodCheck(redisRefreshToken.getRefreshToken())){
            // 4_1. access + refresh 새로 발급
            String new_access = jwtTokenProvider.createAccessToken(authentication);
            String new_refresh = jwtTokenProvider.createRefreshToken(authentication);
            Long date = jwtTokenProvider.getAccessTokenExpiration(new_access);
            tokenDTO = JwtTokenDTO.of("both", new_access, new_refresh, date);
            //refresh 삭제 후 저장
            refreshTokenService.deleteRefreshToken(authentication.getName());
            refreshTokenService.saveRefreshToken(new_refresh, authentication.getName());
        }else{ // 4_2. 유효기간 3일 이상: access만 발급
            String new_accessToken = jwtTokenProvider.createAccessToken(authentication);
            Long date = jwtTokenProvider.getAccessTokenExpiration(new_accessToken);
            tokenDTO = JwtTokenDTO.builder().type("alone").accessToken(new_accessToken).accessTokenExpiresIn(date).build();
        }
        return tokenDTO;
    }

    @Transactional
    public UserInfoResDTO userInfo(String email){
        return memberRepository.findByEmail(email)
                .map(UserInfoResDTO::of)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public List<UserListDTO> getAllUsers(){
        return memberRepository.findAllByRole(Role.USER).stream()
                .map(UserListDTO::fromEntity)
                .collect(Collectors.toList());

    }
    @Transactional
    public List<UserListDTO> getAllAdmins(){
        return memberRepository.findAllByRole(Role.ADMIN).stream()
                .map(UserListDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional
    public void addWarning(MemberWarningReqDTO dto){
        log.info("email 출력: {}", dto.getEmail());
        MemberReputation memberReputation = memberReputationRepository.findMemberReputationByMemberEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        //정지 회원일 경우
        if (memberReputation.getMember().getMemberStatus() == MemberStatus.BANNED){
            throw new CustomException(MemberErrorCode.BANNED_USER);
        }
        //핀 경고 +1
        memberReputation.addPinWarning();
        //자동 경고 평가 & 누적 경고 평가
        memberReputation.evaluateAutoBan();
        memberReputation.evaluateAutoWarning();

    }


}
