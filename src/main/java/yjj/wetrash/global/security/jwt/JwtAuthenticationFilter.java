package yjj.wetrash.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final JwtTokenProvider tokenProvider;

    // 1.요청 낚아채기 2. 서블릿(controller)로 요청 넘기기
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //요청 헤더 에서 토큰 꺼내기
        String jwt = resolveToken(request);
        //유효성 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
            //검증된 회원 정보는 Authentication 객체로 SecurityContext 에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response); //controller 로 요청 전달
    }
    //요청에서 토큰 정보 꺼내는 메서드  Authorization: Bearer 토큰값
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //Authorization 헤더 값 가져옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){ //Bearer 로 시작하는지 확인
            return bearerToken.substring(7); // "Bearer "제거
        }
        return null;
    }
}
