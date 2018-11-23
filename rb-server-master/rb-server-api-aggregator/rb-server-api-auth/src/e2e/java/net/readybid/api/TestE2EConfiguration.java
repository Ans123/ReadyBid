package net.readybid.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.elasticsearch.ElasticSearchOperations;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Profile("e2eTest")
@Configuration
@Import(Application.class)
public class TestE2EConfiguration {

    @Bean
    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        final AmazonSimpleEmailService mock = mock(AmazonSimpleEmailService.class);
        return mock;
    }


    @Bean
    @Primary
    public net.readybid.email.EmailService getEmailService() {
        final net.readybid.email.EmailService mock = mock(net.readybid.email.EmailService.class);
        return mock;
    }


    @Bean("NewEmailService")
    @Primary
    public EmailService getNewEmailService() {
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
