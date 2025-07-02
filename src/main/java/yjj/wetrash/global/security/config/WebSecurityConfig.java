package yjj.wetrash.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yjj.wetrash.global.exception.ErrorResponseWriter;
import yjj.wetrash.global.security.jwt.JwtAuthenticationFilter;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;
import yjj.wetrash.global.security.jwt.exception.JwtAccessDeniedHandler;
import yjj.wetrash.global.security.jwt.exception.JwtAuthenticationEntryPoint;
import yjj.wetrash.global.security.oauth.CustomOAuth2FailureHandler;
import yjj.wetrash.global.security.oauth.CustomOAuth2UserService;
import yjj.wetrash.global.security.oauth.CustomOAuth2SuccessHandler;

@Configuration //스프링 필터 체인 생성
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final ErrorResponseWriter errorResponseWriter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler oAuth2FailureHandler;

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
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(
                                "/api/user/**",
                                "/api/pin/**",
                                "/api/chat/**", "/ws/**",
                                "/uploads/**",
                                "/api/points/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated());
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                );
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                );
        http
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, errorResponseWriter),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




}
