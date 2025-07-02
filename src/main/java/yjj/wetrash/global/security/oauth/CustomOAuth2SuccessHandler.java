package yjj.wetrash.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import yjj.wetrash.domain.member.service.RefreshTokenService;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;
import yjj.wetrash.global.security.util.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;
    private static final String URI = "http://localhost:3000/oauth2/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        //refresh 생성 및 저장
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        refreshTokenService.saveRefreshToken(refreshToken, authentication.getName());
        //refreshToken 쿠키로 감싸기
        ResponseCookie refreshTokenCookie = cookieUtil.createCookie(refreshToken);
        response.addHeader("Set-Cookie", refreshTokenCookie.toString()); //refresh 전송

        //클라이언트 홈으로 이동
        response.sendRedirect(URI);
    }
}
