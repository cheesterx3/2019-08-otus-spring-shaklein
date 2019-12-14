package ru.otus.study.spring.integrationtask.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import ru.otus.study.spring.integrationtask.domain.Accident;
import ru.otus.study.spring.integrationtask.domain.ObjectMessage;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan
public class JpaConfig {
    private final EntityManagerFactory managerFactory;

    @Bean
    public IntegrationFlow messageSaveFlow() {
        return f -> f
                .channel("messageSaveChannel")
                .handle(Jpa.updatingGateway(this.managerFactory)
                                .entityClass(ObjectMessage.class)
                                .persistMode(PersistMode.PERSIST),
                        e -> e.id("messageSaveEndPoint").transactional());
    }

    @Bean
    public IntegrationFlow accidentSaveFlow() {
        return f -> f
                .channel("accidentSaveChannel")
                .handle(Jpa.updatingGateway(this.managerFactory)
                                .entityClass(Accident.class)
                                .persistMode(PersistMode.PERSIST),
                        ConsumerEndpointSpec::transactional);
    }

}
