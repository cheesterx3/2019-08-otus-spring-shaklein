package ru.otus.study.spring.batch.librarymigration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.Book;
import ru.otus.study.spring.batch.librarymigration.domain.jdbc.BookComment;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.BookCommentMongo;
import ru.otus.study.spring.batch.librarymigration.domain.mongo.BookMongo;


@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job migrateJob(Flow preRequisiteMigrateFlow, Step bookMigrateStep, Step bookCommentMigrateStep) {
        return jobBuilderFactory.get("migrateJob")
                .incrementer(parameters -> parameters)
                .start(preRequisiteMigrateFlow)
                .next(bookMigrateStep)
                .next(bookCommentMigrateStep)
                .end()
                .build();
    }

    @Bean
    public Flow preRequisiteMigrateFlow(Flow authorMigrateStep, Flow genreMigrateStep, Flow userInfoMigrateStep) {
        return new FlowBuilder<SimpleFlow>("preRequisiteMigrateFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(authorMigrateStep, genreMigrateStep, userInfoMigrateStep)
                .build();
    }

    @Bean
    public Step bookMigrateStep(ItemReader<Book> bookReader,
                                ItemProcessor<Book, BookMongo> bookProcessor,
                                ItemWriter<BookMongo> bookWriter) {
        return stepBuilderFactory.get("bookMigrateStep")
                .<Book, BookMongo>chunk(3)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step bookCommentMigrateStep(ItemReader<BookComment> bookCommentReader,
                                       ItemProcessor<BookComment, BookCommentMongo> bookCommentProcessor,
                                       ItemWriter<BookCommentMongo> bookCommentWriter) {
        return stepBuilderFactory.get("bookCommentMigrateStep")
                .<BookComment, BookCommentMongo>chunk(3)
                .reader(bookCommentReader)
                .processor(bookCommentProcessor)
                .writer(bookCommentWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JobOperator jobOperator(final JobLauncher jobLauncher, final JobRepository jobRepository,
                                   final JobRegistry jobRegistry, final JobExplorer jobExplorer) {
        final SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobExplorer(jobExplorer);
        return jobOperator;
    }

    @Bean
    public JobFactory jobFactory(Job job, JobRegistry jobRegistry) throws DuplicateJobException {
        final ReferenceJobFactory jobFactory = new ReferenceJobFactory(job);
        jobRegistry.register(jobFactory);
        return jobFactory;
    }

}
