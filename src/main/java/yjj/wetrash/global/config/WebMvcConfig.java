package yjj.wetrash.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-url}")
    private String accessUrl;

    @Value("${file.base-url}")
    private String baseUrl;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 모든 경로에 대해 CORS 적용
                        .allowedOrigins(allowedOrigins)  // 허용할 도메인 (React 등)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // 허용할 HTTP 메서드
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true) // 인증 정보 포함 허용
                        .exposedHeaders("Authorization",  "Content-Type");
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(accessUrl+"**")
                .addResourceLocations("classpath:/static/uploads/");
    }
}
