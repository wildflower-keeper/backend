package org.wildflowergardening.backend.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.wildflowergardening.backend.core"
)
@EnableJpaAuditing
@EntityScan(basePackages = {"org.wildflowergardening.backend.core"})
@EnableTransactionManagement
@RequiredArgsConstructor
public class JpaConfig {
}
