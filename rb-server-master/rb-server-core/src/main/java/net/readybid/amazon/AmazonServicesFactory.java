package net.readybid.amazon;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@Profile("production")
@PropertySource(value="classpath:application.properties")
public class AmazonServicesFactory {

    public static final Regions AWS_REGION = Regions.US_WEST_2;

    private final String key;
    private final String id;

    private final AWSStaticCredentialsProvider credentials;
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final AmazonS3 amazonS3;

    @Autowired
    public AmazonServicesFactory(Environment environment){
        key = environment.getProperty("com.amazon.key");
        id = environment.getProperty("com.amazon.id");

        credentials = createCredentials();
        amazonS3 = createS3();
        amazonSimpleEmailService = createEmailService();
    }

    @Bean
    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        return amazonSimpleEmailService;
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    private AmazonSimpleEmailService createEmailService() {
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withCredentials(credentials)
                .withRegion(AWS_REGION)
                .build();
    }

    private AmazonS3 createS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials)
                .withRegion(AWS_REGION)
                .build();
    }

    private AWSStaticCredentialsProvider createCredentials() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(id, key));
    }
}
