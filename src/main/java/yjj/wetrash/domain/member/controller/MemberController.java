package yjj.wetrash.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.member.dto.LoginReqDTO;
import yjj.wetrash.domain.member.dto.SignUpReqDTO;
import yjj.wetrash.domain.member.dto.UserInfoResDTO;
import yjj.wetrash.domain.member.service.MemberService;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;
import yjj.wetrash.global.security.jwt.dto.JwtTokenReqDTO;
import yjj.wetrash.global.security.util.CookieUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Member API", description = "사용자 관련 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDTO signUpDTO){
        memberService.signUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/signIn")
    public ResponseEntity<JwtTokenDTO> signIn(@RequestBody @Valid LoginReqDTO loginReqDTO){
        JwtTokenDTO token = memberService.signIn(loginReqDTO);
        ResponseCookie createCookie = cookieUtil.createCookie(token.getRefreshToken());
        token.setRefreshToken("");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createCookie.toString())
                .body(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenDTO> reissue(@CookieValue(value = "refreshToken") String refreshToken){
        log.info("refresh: {}", refreshToken);
        JwtTokenDTO token = memberService.reissue(refreshToken);
        if (token.getType()=="both"){
            ResponseCookie createCookie = cookieUtil.createCookie(token.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, createCookie.toString())
                    .body(token);
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoResDTO> userInfo(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        UserInfoResDTO userInfo = memberService.userInfo(email);
        return ResponseEntity.ok(userInfo);
    }

}
