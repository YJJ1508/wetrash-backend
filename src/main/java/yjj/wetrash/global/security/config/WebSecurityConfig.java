package yjj.wetrash.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import yjj.wetrash.global.security.jwt.JwtAuthenticationFilter;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;

import java.util.List;

@Configuration //스프링 필터 체인 생성
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private static final String[] SWAGGER = {
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**",
            "/webjars/**", "/swagger-resources/**", "/swagger-resources", "/configuration/ui",
            "/configuration/security"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .anyRequest().authenticated());
        http
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
