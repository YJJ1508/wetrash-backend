package yjj.wetrash.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final String URI = "http://localhost:3000/signIn";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("failure handler: {}", exception.getMessage());
        String[] parts = exception.getMessage().split(":");
        String errorCode = parts[0];
        String encodedEmail = Base64.getEncoder().encodeToString(parts[1].getBytes());
        String redirectUrl = UriComponentsBuilder
                .fromHttpUrl(URI)
                .queryParam("error", errorCode)
                .queryParam("email", encodedEmail)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
