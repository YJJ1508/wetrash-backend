package yjj.wetrash.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "yjj.wetrash.domain.member.repository",
                "yjj.wetrash.domain.pin.repository",
                "yjj.wetrash.domain.chat.repository",
        }
)
@EntityScan(
        basePackages = {
                "yjj.wetrash.domain.member.entity",
                "yjj.wetrash.domain.pointHistory.entity",
                "yjj.wetrash.domain.pin.entity",
                "yjj.wetrash.domain.pinReview.entity",
                "yjj.wetrash.domain.chat.entity",
        }
)
public class JpaConfig {
}
