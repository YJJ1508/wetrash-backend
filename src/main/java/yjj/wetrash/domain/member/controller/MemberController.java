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
import org.springframework.web.multipart.MultipartFile;
import yjj.wetrash.domain.member.dto.*;
import yjj.wetrash.domain.member.dto.mypage.MemberProfileResDTO;
import yjj.wetrash.domain.member.dto.mypage.NicknameCheckReqDTO;
import yjj.wetrash.domain.member.service.MemberMyPageService;
import yjj.wetrash.domain.member.service.MemberService;
import yjj.wetrash.domain.member.service.PointHistoryService;
import yjj.wetrash.domain.member.util.ProfileImgUploader;
import yjj.wetrash.global.security.CustomDetails;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;
import yjj.wetrash.global.security.jwt.dto.JwtTokenReqDTO;
import yjj.wetrash.global.security.util.CookieUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Member API", description = "사용자 관련 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final CookieUtil cookieUtil;
    private final PointHistoryService pointHistoryService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestPart("user-data") @Valid SignUpReqDTO signUpDTO,
                                    @RequestPart(value = "profile-data", required = false) MultipartFile profile){
        log.info("profile-data: {}", profile);
        memberService.signUp(signUpDTO, profile);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }



    @PostMapping("/signIn")
    public ResponseEntity<JwtTokenDTO> signIn(@RequestBody @Valid LoginReqDTO loginReqDTO){
        log.info("signin controller");
        JwtTokenDTO token = memberService.signIn(loginReqDTO);
        log.info("service 통과");
        ResponseCookie createCookie = cookieUtil.createCookie(token.getRefreshToken());
        token.setRefreshToken("");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createCookie.toString())
                .body(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenDTO> reissue(@CookieValue(value = "refreshToken") String refreshToken){
        log.info("수령받은 refresh: {}", refreshToken);
        JwtTokenDTO token = memberService.reissue(refreshToken);
        if ("both".equals(token.getType())){
            ResponseCookie createCookie = cookieUtil.createCookie(token.getRefreshToken());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, createCookie.toString())
                    .body(token);
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me") //상단바 용 (단순 정보)
    public ResponseEntity<UserInfoResDTO> userInfo(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getUsername();
        UserInfoResDTO userInfo = memberService.userInfo(email);
        return ResponseEntity.ok(userInfo);
    }



}
