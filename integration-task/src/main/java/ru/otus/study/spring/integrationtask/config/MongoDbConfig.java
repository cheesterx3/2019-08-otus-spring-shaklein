package ru.otus.study.spring.integrationtask.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import ru.otus.study.spring.integrationtask.domain.MongoAccident;
import ru.otus.study.spring.integrationtask.service.MongoStoreService;

@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan
public class MongoDbConfig {
    private final MongoStoreService storeService;

    @Bean
    public IntegrationFlow mongoAccidentSaveFlow() {
        return f -> f
                .channel("mongoSaveChannel")
                .<MongoAccident>handle((payload, headers) -> storeService.save(payload), e -> e.id("mongoSaveEndpoint"));
    }

}
