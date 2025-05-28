package yjj.wetrash.domain.pin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.pin.entity.Pin;
import yjj.wetrash.domain.pin.entity.PinFavorite;
import yjj.wetrash.domain.pin.entity.PinReview;
import yjj.wetrash.domain.pin.exception.PinErrorCode;
import yjj.wetrash.domain.pin.repository.PinFavoriteRepository;
import yjj.wetrash.domain.pin.repository.PinRepository;
import yjj.wetrash.domain.pin.repository.PinReviewRepository;
import yjj.wetrash.global.exception.CustomException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PinFavoriteService {

    private final MemberRepository memberRepository;
    private final PinRepository pinRepository;
    private final PinFavoriteRepository pinFavoriteRepository;

    @Transactional
    public void togglePinFavorite(String email, Long pinId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        Pin pin = pinRepository.findPinById(pinId)
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));

        Optional<PinFavorite> pinFavoriteOpt = pinFavoriteRepository.findByMemberAndPin(member, pin);

        if(pinFavoriteOpt.isPresent()){
            pinFavoriteRepository.delete(pinFavoriteOpt.get());
        } else {
            PinFavorite pinFavorite = PinFavorite.builder()
                    .member(member)
                    .pin(pin)
                    .build();
            pinFavoriteRepository.save(pinFavorite);
        }
    }

    public boolean isPinFavorite(String email, Long pinId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        Pin pin = pinRepository.findPinById(pinId)
                .orElseThrow(() -> new CustomException(PinErrorCode.PIN_NOT_FOUND));
        boolean isPinFavorite = pinFavoriteRepository.existsPinFavoriteByMemberAndPin(member, pin);
        return isPinFavorite;
    }

}
