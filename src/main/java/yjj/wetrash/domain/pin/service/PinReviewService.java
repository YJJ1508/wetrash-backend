package yjj.wetrash.domain.pin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.pin.dto.PinReviewReqDTO;
import yjj.wetrash.domain.pin.dto.PinReviewUpdateReqDTO;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;
import yjj.wetrash.domain.pin.exception.PinErrorCode;
import yjj.wetrash.domain.pin.repository.PinRepository;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;
import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.security.CustomDetails;

@Service
@RequiredArgsConstructor
public class PinReviewService {

    private final MemberRepository memberRepository;
    private final PinRepository pinRepository;
    private final PinReviewRepository pinReviewRepository;

    @Transactional
    public void savePinReview(String email, PinReviewReqDTO pinReviewReqDTO){
        //댓글 단 회원 조회
        Member member = getMember(email);
        //pin 조회
        Pin pin = pinRepository.findPinById(pinReviewReqDTO.getPinId())
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
        PinReview pinReview = pinReviewReqDTO.toEntity(member, pin);
        pinReviewRepository.save(pinReview);
    }

    @Transactional
    public void updatePinReview(String email, Long reviewId, PinReviewUpdateReqDTO updateReqDTO){
        PinReview pinReview = pinReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_REVIEW_NOT_FOUND));
        //작성자 검증
        Long memberId = getMember(email).getId();
        if (!pinReview.getMember().getId().equals(memberId)){
            throw new CustomException(PinErrorCode.PIN_REVIEW_UPDATE_FORBIDDEN);
        }
        pinReview.updateComment(updateReqDTO);
    }

    @Transactional
    public void deletePinReview(String email, Long reviewId){
        PinReview pinReview = pinReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_REVIEW_NOT_FOUND));
        //작성자 검증
        Long memberId = getMember(email).getId();
        if (!pinReview.getMember().getId().equals(memberId)){
            throw new CustomException(PinErrorCode.PIN_REVIEW_DELETE_FORBIDDEN);
        }
        pinReviewRepository.delete(pinReview);
    }

    private Member getMember(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
    }

}
