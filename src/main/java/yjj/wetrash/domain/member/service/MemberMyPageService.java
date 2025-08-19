package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yjj.wetrash.domain.member.dto.mypage.*;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.util.S3ProfileUploader;
import yjj.wetrash.domain.pin.repository.PinFavoriteRepository;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;
import yjj.wetrash.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberMyPageService {

    private final MemberRepository memberRepository;
    private final S3ProfileUploader s3ProfileUploader;
    private final PinReviewRepository pinReviewRepository;
    private final PinFavoriteRepository pinFavoriteRepository;
    private final PointHistoryService pointHistoryService;

    private Member getMember(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
    }

    //회원 기본 정보 조회
    public MemberProfileResDTO getMyProfileInfo(String email) {
        Member member = getMember(email);
        return MemberProfileResDTO.from(member);
    }

    //닉네임 변경 중복 체크
    public boolean checkNicknameDuplicate(String nickname){
        return memberRepository.existsByNickname(nickname);
    }
    //닉네임 수정
    @Transactional
    public void updateNickname(String email, String nickname){
        if (memberRepository.existsByNickname(nickname)){
            throw new CustomException(MemberErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }
        Member member = getMember(email);
        member.updateNickname(nickname);
    }

    //프로필 수정
    @Transactional
    public void updateProfile(String email, MultipartFile multipartFile){
        Member member = getMember(email);
        String profile = s3ProfileUploader.uploadFile(multipartFile);
        member.updateProfile(profile);
    }

    //내 리뷰 조회
    @Transactional
    public List<ReviewsResDTO> getMemberReviews(String email){
        Member member = getMember(email);
        return pinReviewRepository.findAllByMember(member).stream()
                .map(ReviewsResDTO::from)
                .collect(Collectors.toList());
    }

    //내 즐겨찾기 조회
    @Transactional
    public List<FavoritesResDTO> getMemberFavorites(String email){
        Member member = getMember(email);
        return pinFavoriteRepository.findAllByMember(member).stream()
                .map(FavoritesResDTO::from)
                .collect(Collectors.toList());
    }

    //내 등급 조회
    @Transactional
    public PointResDTO getMemberPointTier(String email){ //dto: 회원 점수, 등급
        Member member = getMember(email);
        String tier = pointHistoryService.calculateTier(member);
        return PointResDTO.builder()
                .totalPoint(member.getTotalPoint())
                .tier(tier)
                .build();
    }
    //등급 상세 적립 내역 조회
    @Transactional
    public List<PointDetailResDTO> getMemberPointDetail(String email){
        Member member = getMember(email);
        return pointHistoryService.getAllPoints(member).stream()
                .map(PointDetailResDTO::from)
                .collect(Collectors.toList());
    }

    //회원 탈퇴 (논리 삭제)
    @Transactional
    public void withdrawMember(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        member.setStatusToWithdrawn(member.getMemberStatus());
    }


}
