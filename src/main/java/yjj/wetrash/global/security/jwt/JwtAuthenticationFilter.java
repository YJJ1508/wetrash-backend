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
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.global.exception.ErrorResponse;
import yjj.wetrash.global.exception.ErrorResponseWriter;
import yjj.wetrash.global.security.CustomDetails;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final JwtTokenProvider tokenProvider;
    private final ErrorResponseWriter errorResponseWriter;

    // 1.요청 낚아채기 2. 서블릿(controller)로 요청 넘기기
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //요청 헤더 에서 토큰 꺼내기
        String jwt = resolveToken(request);
        //유효성 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            //정지 회원 여부 확인
            if (!"GET".equalsIgnoreCase(request.getMethod()) && //get api만 허용
                checkSuspended(response, authentication)) {
                return;
            }
            //검증된 회원 정보는 Authentication 객체로 SecurityContext 에 저장
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
    //회원 상태 확인: 정지 회원 제한
    private boolean checkSuspended(HttpServletResponse response, Authentication authentication) throws IOException{
        CustomDetails customDetails = (CustomDetails) authentication.getPrincipal();
        MemberStatus memberStatus = customDetails.getMemberStatus();
        log.info("필터에서 회원 상태 출력: {}", memberStatus);
        //필터라 response 수동으로 만들어서 전송(403 에러코드를 받지 못함)
        if (memberStatus == MemberStatus.SUSPENDED){
            errorResponseWriter.write(response, 403,
                    new ErrorResponse(403, "정지된 회원으로, 일부 기능이 제한됩니다."));
            return true;
        }
        return false;
    }

}
