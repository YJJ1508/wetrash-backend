package yjj.wetrash.domain.member.service;

import jakarta.el.ELManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yjj.wetrash.domain.member.dto.mypage.MemberProfileResDTO;
import yjj.wetrash.domain.member.dto.mypage.NicknameCheckReqDTO;
import yjj.wetrash.domain.member.dto.mypage.ReviewsResDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.util.ProfileImgUploader;
import yjj.wetrash.domain.pin.entity.PinReview;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;
import yjj.wetrash.domain.pin.service.PinReviewService;
import yjj.wetrash.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberMyPageService {

    private final MemberRepository memberRepository;
    private final ProfileImgUploader profileImgUploader;
    private final PinReviewRepository pinReviewRepository;

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

    //프로필 수정
    @Transactional
    public void updateProfile(String email, MultipartFile multipartFile){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        String profile = profileImgUploader.saveFile(multipartFile);
        member.updateProfile(profile);
    }

    //내 리뷰 조회
    @Transactional
    public List<ReviewsResDTO> getMemberReviews(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        return pinReviewRepository.findAllByMember(member).stream()
                .map(ReviewsResDTO::from)
                .collect(Collectors.toList());
    }


}
