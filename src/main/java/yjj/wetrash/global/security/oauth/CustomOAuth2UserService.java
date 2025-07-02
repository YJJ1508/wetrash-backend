package yjj.wetrash.global.security.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.MemberStatus;
import yjj.wetrash.domain.member.exception.MemberErrorCode;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.MemberReputationRepository;
import yjj.wetrash.global.exception.CustomException;
import yjj.wetrash.global.security.CustomDetails;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberReputationRepository memberReputationRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // provider api 로부터 유저 정보 가져 오기
        Map<String, Object> oauth2UserAttributes = super.loadUser(userRequest).getAttributes();
        // 어느 소셜인지 구분하는 정보 (google, kakao)
        String providerId = userRequest.getClientRegistration().getRegistrationId();
        //회원의 고유 id 가져올때 사용되는 키 값 가져오기. (구글: sub)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        //DTO로 정보 가공
        OAuth2UserDTO auth2UserDTO = OAuth2UserDTO.of(providerId, oauth2UserAttributes, userNameAttributeName);
        //회원가입 or 로그인
        Member member = getOrSave(auth2UserDTO);
        if (member.getMemberStatus() == MemberStatus.WITHDRAWN){
            OAuth2Error error = new OAuth2Error("WITHDRAWN_USER");
            throw new OAuth2AuthenticationException(error, "WITHDRAWN_USER:" + member.getEmail());
        }
        //회원 평판 생성 (부가 정보)
        if (!reputationExists(member)){
            memberReputationRepository.save(member.createReputation());
        }
        //OAuth2User 객체 반환(security context 저장)
        return new CustomDetails(member, oauth2UserAttributes);
    }
    private Member getOrSave(OAuth2UserDTO userDTO){
        return memberRepository.findByEmail(userDTO.email())    //기존 회원 -> 멤버 반환
                .orElseGet(() -> memberRepository.save(userDTO.toEntity())); //기존 회원X -> 회원가입
    }

    private boolean reputationExists(Member member){
        if (memberReputationRepository.existsMemberReputationByMember(member)) return true;
        else return false;
    }


}
