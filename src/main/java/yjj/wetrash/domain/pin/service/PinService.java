package yjj.wetrash.domain.pin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.pin.dto.PinRequestDTO;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinStatus;
import yjj.wetrash.domain.pin.exception.PinErrorCode;
import yjj.wetrash.domain.pin.repository.PinRepository;
import yjj.wetrash.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PinService {

    private final MemberRepository memberRepository;
    private final PinRepository pinRepository;
    private static final double DUPLICATE_DISTANCE = 50.0; //50m 기준 반경

    @Transactional
    public void savePin(String email, PinRequestDTO pinRequestDTO){
        //1. 사용자 검증
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        if (member.getMemberStatus() == MemberStatus.WARNING){ //경고 회원 요청 막기
            throw new CustomException(MemberErrorCode.WARNING_USER);
        }
        //2. 중복 핀 검증
        if (isDuplicatePin(pinRequestDTO.getLatitude(), pinRequestDTO.getLongitude())) {
            throw new CustomException(PinErrorCode.PIN_ALREADY_EXISTS);
        }
        //3. 핀 저장 - (핀상태: pending)
        pinRepository.save(pinRequestDTO.toEntity(member));
    }
    // 위도,경도 50m 반경 같은 위치로 처리 (Haversine 공식)
    private boolean isDuplicatePin(double lat, double lng){
        final int EARTH_RADIUS = 6371;
        //기존 핀 db에서 가져오기
        List<Pin> approvedPins = pinRepository.findAllByStatus(PinStatus.APPROVED);
        for (Pin pin: approvedPins){
            double latDistance = Math.toRadians(pin.getLatitude() - lat);
            double lonDistance = Math.toRadians(pin.getLongitude() - lng);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(pin.getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = EARTH_RADIUS * c * 1000;
            if (distance <= DUPLICATE_DISTANCE){
                return true;
            }
        }
        return false;
    }



}
