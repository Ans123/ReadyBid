package net.readybid.api.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.elasticsearch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by DejanK on 9/7/2016.
 *
 */
@Configuration
@Profile("production")
@PropertySource(value="classpath:application.properties")
public class ElasticSearchConfiguration {

    private final ObjectMapper mapper;
    private final String elasticSearchServerProtocol;
    private final String elasticSearchServerHostname;
    private final int elasticSearchServerPort;

    @Autowired
    public ElasticSearchConfiguration(Environment env, ObjectMapper mapper) {
        this.mapper = mapper;
        elasticSearchServerProtocol = env.getProperty("elasticsearch.protocol");
        elasticSearchServerHostname = env.getProperty("elasticsearch.hostname");
        elasticSearchServerPort = Integer.parseInt(env.getProperty("elasticsearch.port"));
    }

    @Bean
    public ElasticSearchOperations elasticSearchOperations(){
        AwsElasticSearchFactory factory = new AwsElasticSearchFactory(elasticSearchServerHostname, elasticSearchServerPort, elasticSearchServerProtocol);
        ElasticSearchConverter converter = new ElasticSearchConverterImpl(mapper);
        return new ElasticSearchTemplate(factory, converter);
    }
}
