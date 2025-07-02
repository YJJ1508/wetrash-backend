package yjj.wetrash.domain.pin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.entity.PointHistory.PointReason;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
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
@Slf4j
public class PinReviewService {

    private final MemberRepository memberRepository;
    private final PinRepository pinRepository;
    private final PinReviewRepository pinReviewRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void savePinReview(String email, PinReviewReqDTO pinReviewReqDTO){
        //댓글 단 회원 조회
        Member member = getMember(email);
        //pin 조회
        Pin pin = pinRepository.findPinById(pinReviewReqDTO.getPinId())
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
        PinReview pinReview = pinReviewReqDTO.toEntity(member, pin);
        pinReviewRepository.save(pinReview);
        log.info("포인트 적립 전");
        //포인트 적립
        if (!pointHistoryRepository.existsByMemberAndPinAndPointReason(member, pin, PointReason.REVIEW)){
            log.info("포인트 적립 if문 들어옴");
            PointHistory pointHistory = PointHistory.createForReview(member, pin);
            pointHistoryRepository.save(pointHistory);
            member.addPoint(pointHistory.getPoint()); //회원 포인트 add
        }
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
        Member member = getMember(email);
        if (!pinReview.getMember().getId().equals(member.getId())){
            throw new CustomException(PinErrorCode.PIN_REVIEW_DELETE_FORBIDDEN);
        }
        pinReviewRepository.delete(pinReview);

        //포인트 차감
        Pin pin = pinReview.getPin();
        if (pointHistoryRepository.existsByMemberAndPinAndPointReason(member, pin, PointReason.REVIEW)){
            //해당 포인트 등록 객체 삭제
            PointHistory pointHistory = pointHistoryRepository.findByMemberAndPinAndPointReason(
                    member, pin, PointReason.REVIEW)
                    .orElseThrow(() -> new CustomException(MemberErrorCode.POINT_HISTORY_NOT_FOUND));
            pointHistoryRepository.delete(pointHistory);
            member.subtractPoint(pointHistory.getPoint());
        }
    }

    private Member getMember(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
    }

}
