package yjj.wetrash.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition(
        info = @Info(
                title = "WE_TRASH API 명세서",
                description = "WE_TRASH API 명세서입니다.",
                version = "v1"
        )
)
@Configuration
@EnableWebMvc
public class SwaggerConfig {

//    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

//    @Bean
//    @Profile("!Prod") //운영에서는 swagger 비활성화
//    public OpenAPI customOpenAPI(){
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
//        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
//                                .name(SECURITY_SCHEME_NAME)
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("Bearer")
//                                .bearerFormat("JWT")));
//    }
private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    // 운영 환경에는 Swagger를 비활성화하기 위해 추가했습니다.
    @Profile("!Prod")
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                        .name(jwt)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(BEARER_TOKEN_PREFIX)
                        .bearerFormat("JWT"));

        // Swagger UI 접속 후, 딱 한 번만 accessToken을 입력해주면 모든 API에 토큰 인증 작업이 적용됩니다.
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
