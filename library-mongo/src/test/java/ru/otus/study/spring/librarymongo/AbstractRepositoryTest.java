package ru.otus.study.spring.librarymongo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.study.spring.librarymongo.config", "ru.otus.study.spring.librarymongo.repository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractRepositoryTest {
}
