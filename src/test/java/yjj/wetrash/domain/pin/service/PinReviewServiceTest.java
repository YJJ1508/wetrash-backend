package yjj.wetrash.domain.pin.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
import yjj.wetrash.domain.pin.dto.PinReviewReqDTO;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinReview;
import yjj.wetrash.domain.pin.repository.PinRepository;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;

//@ExtendWith(MockitoExtension.class)
//public class PinReviewServiceTest {
//
//    @Mock
//    private MemberRepository memberRepository;
//    @Mock
//    private PinRepository pinRepository;
//    @Mock
//    private PinReviewRepository pinReviewRepository;
//    @Mock
//    private PointHistoryRepository pointHistoryRepository;
//    @InjectMocks
//    private PinReviewService pinReviewService;
//
//    @Test
//    @DisplayName("포인트 적립 확인하기")
//    void savePinReviewTest(){
//        // given
//        Member member = memberRepository.save(Member.builder()
//                .email("commentMail@test.com")
//                .nickname("댓글")
//                .totalPoint(0)
//                .build());
//        Pin pin = pinRepository.save(Pin.builder()
//                .title("title")
//                .build());
//        System.out.println("pinId: "+pin.getId());
//        PinReviewReqDTO pinReviewReqDTO = PinReviewReqDTO.builder()
//                .comment("댓글입니다")
//                .pinId(pin.getId())
//                .build();
//        // when
//        pinReviewService.savePinReview(member.getEmail(), pinReviewReqDTO);
//        // then
//        boolean exists = pointHistoryRepository.existsByMemberAndPinAndPointReason(
//                member, pin, PointHistory.PointReason.REVIEW
//        );
//        Assertions.assertThat(exists).isTrue();
//        Assertions.assertThat(member.getTotalPoint()).isEqualTo(5);
//    }
//
//}
