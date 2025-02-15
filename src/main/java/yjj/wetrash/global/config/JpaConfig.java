package yjj.wetrash.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "yjj.wetrash.domain.member.repository")
@EntityScan(basePackages = "yjj.wetrash.domain.member.entity")
public class JpaConfig {
}
