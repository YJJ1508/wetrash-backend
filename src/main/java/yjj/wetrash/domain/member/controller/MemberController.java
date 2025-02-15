package yjj.wetrash.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjj.wetrash.domain.member.dto.LoginReqDTO;
import yjj.wetrash.domain.member.dto.SignUpReqDTO;
import yjj.wetrash.domain.member.service.MemberService;
import yjj.wetrash.global.security.jwt.dto.JwtTokenDTO;
import yjj.wetrash.global.security.jwt.dto.JwtTokenReqDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Member API", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDTO signUpDTO){
        memberService.signUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/signIn")
    public ResponseEntity<JwtTokenDTO> login(@RequestBody @Valid LoginReqDTO loginReqDTO){
        JwtTokenDTO token = memberService.login(loginReqDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenDTO> reissue(@RequestBody JwtTokenReqDTO jwtTokenReqDTO){
        JwtTokenDTO token = memberService.reissue(jwtTokenReqDTO);
        return ResponseEntity.ok(token);
    }

}
