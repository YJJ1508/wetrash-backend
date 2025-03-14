package yjj.wetrash.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;
import yjj.wetrash.global.security.util.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private static final String URI = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        //refreshToken 쿠키로 감싸기
        ResponseCookie refreshTokenCookie = cookieUtil.createCookie(refreshToken);
        response.addHeader("Set-Cookie", refreshTokenCookie.toString()); //refresh 전송

        //클라이언트 홈으로 이동
        response.sendRedirect(URI);
    }
}
