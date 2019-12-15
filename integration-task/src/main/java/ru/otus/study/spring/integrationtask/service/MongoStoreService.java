package ru.otus.study.spring.integrationtask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.endpoint.IntegrationConsumer;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.integrationtask.domain.AbstractAccident;
import ru.otus.study.spring.integrationtask.domain.MongoAccident;

@Service
@RequiredArgsConstructor
public class MongoStoreService{
    private final MongoTemplate mongoTemplate;

    public AbstractAccident save(MongoAccident accident) {
        return mongoTemplate.save(accident);
    }
}
