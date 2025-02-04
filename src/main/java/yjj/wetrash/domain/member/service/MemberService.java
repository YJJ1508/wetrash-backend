package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.dto.SignUpDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //회원가입
    @Transactional
    public void signUp(SignUpDTO signUpDTO){
        //회원 중복 검사
        if (memberRepository.existsByEmail(signUpDTO.getEmail())){
            throw new CustomException(MemberErrorCode.USER_ALREADY_EXISTS);
        }
        //비번 암호화
        String encP = bCryptPasswordEncoder.encode(signUpDTO.getPassword());
        //save
        memberRepository.save(signUpDTO.toEntity(encP));
    }
}
