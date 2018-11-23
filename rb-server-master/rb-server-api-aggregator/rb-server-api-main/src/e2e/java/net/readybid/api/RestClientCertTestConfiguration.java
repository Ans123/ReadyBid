package net.readybid.api;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
@Profile("e2eTest")
public class RestClientCertTestConfiguration {

    private char[] password = "4tak18i".toCharArray();

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {

        final SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(ResourceUtils.getFile("keystore.p12"), password, password)
                .build();

        final HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        final ClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);

        return builder.requestFactory(() -> httpRequestFactory).build();
    }
}
