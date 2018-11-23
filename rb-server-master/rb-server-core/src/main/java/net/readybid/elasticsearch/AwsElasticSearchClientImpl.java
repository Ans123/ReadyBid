package net.readybid.elasticsearch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public class AwsElasticSearchClientImpl implements AwsElasticSearchClient {

    private final RestClient restClient;

    public AwsElasticSearchClientImpl(String hostname, int port, String protocol) {
        restClient = RestClient.builder(
                new HttpHost(hostname, port, protocol))
                .build();
    }

    @Override
    public Response get(String index, HttpEntity httpEntity) throws IOException {
        return restClient.performRequest("GET", formatEndpoint(index, "_search/template"), Collections.emptyMap(), httpEntity);
    }

    private String formatEndpoint(String index, String action) {
        return String.format("/%s/%s", allIfEmpty(index), action);
    }

    private String allIfEmpty(String typeOrIndex) {
        return typeOrIndex == null || typeOrIndex.isEmpty() ? "_all" : typeOrIndex;
    }

    @Override
    public Response post(String index, String action, Map<String, String> params, HttpEntity httpEntity) throws IOException {
        return restClient.performRequest("POST", formatEndpoint(index, action), params, httpEntity);
    }
}
