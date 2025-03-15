package yjj.wetrash.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//사용자의 인증 정보를 담는 인터페이스
public class CustomDetails implements UserDetails, OAuth2User{

    private Member member;
    private Map<String, Object> attributes; //oauth provider 로 받은 원본 데이터


    //자체로그인
    public CustomDetails(Member member){
        this.member = member;
    }
    //소셜로그인
    public CustomDetails(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() { //소셜 로그인
        return member.getEmail();
    }
    @Override
    public String getUsername() { //자체 로그인
        return member.getEmail();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
        return authorities;
    }
    public Role getRole(){
        return member.getRole();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }



}
