package yjj.wetrash.domain.pin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.dto.AdminMemberReputationResDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberReputation;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.entity.Role;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.MemberReputationRepository;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
import yjj.wetrash.domain.pin.dto.PinAdminResDTO;
import yjj.wetrash.domain.pin.dto.PinAdminUpdateReqDTO;
import yjj.wetrash.domain.pin.dto.PinApprovalReqDTO;
import yjj.wetrash.domain.pin.dto.PinRejectionReqDTO;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;
import yjj.wetrash.domain.pin.exception.PinErrorCode;
import yjj.wetrash.domain.pin.repository.PinRepository;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;
import yjj.wetrash.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinAdminService {

    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;
    private final MemberReputationRepository memberReputationRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public List<PinAdminResDTO> getPinsByStatus(String status){
        PinStatus pinStatus = PinStatus.valueOf(status.toUpperCase());
        return pinRepository.findAllByStatus(pinStatus).stream()
                .map(PinAdminResDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //관리자 승인 요청 핀 -> update
    @Transactional
    public void updateApprovalPins(List<PinApprovalReqDTO> dtos){
        for (PinApprovalReqDTO  dto: dtos) {
            // 1.pin 업데이트
            Pin pin = pinRepository.findPinById(dto.getId())
                    .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
            pin.updateOnApprove(dto.getTitle(), dto.getDescription()); // title, description, status 수정
            // 2.요청 회원 reputation 반영
            MemberReputation memberR = memberReputationRepository.findMemberReputationByMemberEmail(dto.getEmail())
                    .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
            if (memberR.getMember().getRole() == Role.ADMIN){
                return; //관리자 무시
            }
            memberR.approval();
            //3.사용자 포인트 적립
            Member member = memberRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
            PointHistory pointHistory = PointHistory.createForPinApproval(member, pin);
            pointHistoryRepository.save(pointHistory);
            member.addPoint(pointHistory.getPoint()); //회원 포인트 +
        }
    }

    @Transactional
    public void updateRejectionPins(List<PinRejectionReqDTO> dtos) {
        for (PinRejectionReqDTO dto: dtos){
            // 1.pin 업데이트
            Pin pin = pinRepository.findPinById(dto.getId())
                    .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
            pin.updateOnReject();
            // 2.요청 회원 reputation 반영
            MemberReputation memberR = memberReputationRepository.findMemberReputationByMemberEmail(dto.getEmail())
                    .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
            if (memberR.getMember().getRole() == Role.ADMIN){
                return; //관리자 무시
            }
            memberR.rejection();
        }
    }

    @Transactional
    public void updateApprovedPinsByAdmin(List<PinAdminUpdateReqDTO> dtos){
        dtos.forEach(pinDTO -> {
            Pin pin = pinRepository.findPinById(pinDTO.getId())
                    .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
            pin.updateApprovedPinByAdmin(pinDTO);
        });
    }

    @Transactional
    public void updatePinStateToRemoved(List<Long> pinIds){
        log.info("pinIds: {}", pinIds);
        pinIds.forEach(pinId -> {
            log.info("pinId: {}", pinId);
           Pin pin = pinRepository.findPinById(pinId)
                   .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
           pin.updateOnRemoved();
        });
    }

    @Transactional
    public void updatePinStateToApproved(List<Long> pinIds){
        pinIds.forEach(pinId -> {
            Pin pin = pinRepository.findPinById(pinId)
                    .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
            pin.updateOnApproved();
        });
    }

    @Transactional
    public void deleteApprovedPins(List<Long> pinIds){
        pinIds.forEach(pinId -> {
            Pin pin = pinRepository.findPinById(pinId)
                    .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
            PointHistory pointHistory = pointHistoryRepository.findByPin(pin)
                    .orElseThrow(() -> new CustomException(MemberErrorCode.POINT_HISTORY_NOT_FOUND));
            pointHistory.setPinToNull();
            pinRepository.delete(pin);
        });
    }

    @Transactional
    public List<AdminMemberReputationResDTO> getMemberReputations(){
        return memberReputationRepository.findAll()
                .stream()
                .filter(mr -> mr.getMember().getRole() == Role.USER)
                .map(AdminMemberReputationResDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
