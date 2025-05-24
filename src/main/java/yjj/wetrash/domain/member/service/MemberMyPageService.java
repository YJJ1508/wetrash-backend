package yjj.wetrash.domain.member.service;

import jakarta.el.ELManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.dto.mypage.MemberProfileResDTO;
import yjj.wetrash.domain.member.dto.mypage.NicknameCheckReqDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class MemberMyPageService {

    private final MemberRepository memberRepository;

    //회원 기본 정보 조회
    public MemberProfileResDTO getMyProfileInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        return MemberProfileResDTO.from(member);
    }

    //닉네임 변경 중복 체크
    public boolean checkNicknameDuplicate(String nickname){
        return memberRepository.existsByNickname(nickname);
    }
    //닉네임 수정
    @Transactional
    public void updateNickname(String email, String nickname){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        member.updateNickname(nickname);
    }

}
