package net.readybid.api.auth.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.readybid.jackson.LocalDateJacksonDeserializer;
import net.readybid.jackson.LocalDateJacksonSerializer;
import net.readybid.jackson.ObjectIdJacksonSerializer;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Created by DejanK on 1/18/2017.
 *
 */
@Configuration
public class JacksonConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            final ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(ObjectId.class, new ObjectIdJacksonSerializer(ObjectId.class));
            module.addSerializer(LocalDate.class, new LocalDateJacksonSerializer(LocalDate.class));
            module.addDeserializer(LocalDate.class, new LocalDateJacksonDeserializer(LocalDate.class));
            mapper.registerModule(module);

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper;
        }
}
