package org.wildflowergardening.backend.batch.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Value("${spring.datasource-batch.url}")
    private String batchUrl;

    @Value("${spring.datasource-batch.username}")
    private String batchUsername;

    @Value("${spring.datasource-batch.password}")
    private String batchPassword;


    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public DataSource batchDataSource() {
        return DataSourceBuilder.create()
                .url(batchUrl)
                .username(batchUsername)
                .password(batchPassword)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager(@Qualifier("batchDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
