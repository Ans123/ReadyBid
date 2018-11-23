package net.readybid.elasticsearch;

import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public interface ElasticSearchClient {
    Response get(String index, HttpEntity httpEntity) throws IOException;
    Response post(String index, String path, Map<String, String> params, HttpEntity httpEntity) throws IOException;
}
