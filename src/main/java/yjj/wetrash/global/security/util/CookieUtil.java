package yjj.wetrash.global.security.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public ResponseCookie createCookie(String refreshToken){
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) //XSS 방지
                //.secure(true)
                .path("/")
                .maxAge(7*24*60*60)
                .sameSite("Strict") //csrf 방지
                .build();
    }
}
