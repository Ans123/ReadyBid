package net.readybid.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.mongodb.client.MongoDatabase;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.email.EmailService;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Profile("test")
@Configuration
@Import(Application.class)
public class TestConfiguration {

    @Bean(name = "mongoDatabaseMain")
    public MongoDatabase getDatabase() {
        final MongoDatabase mock = mock(MongoDatabase.class);
        return mock;
    }

    @Bean
    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        final AmazonSimpleEmailService mock = mock(AmazonSimpleEmailService.class);
        return mock;
    }

    @Bean
    @Primary
    public EmailService getEmailService() {
        final EmailService mock = mock(EmailService.class);
        return mock;
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        final AmazonS3 mock = mock(AmazonS3.class);
        return mock;
    }

    @Bean
    public ElasticSearchOperations getElasticSearchOperations() {
        final ElasticSearchOperations mock = mock(ElasticSearchOperations.class);
        return mock;
    }
}
