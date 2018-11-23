package net.readybid.elasticsearch;

import net.readybid.utils.ListResult;
import org.elasticsearch.client.Response;

import java.util.Map;

/**
 * Created by DejanK on 11/2/2016.
 *
 */
public interface ElasticSearchConverter {
    <T> ElasticSearchResult<T> convert(Response response, Class<T> tClass);
    <T> ListResult<T> convert(Response response, EsCodec<T> codec);

    String toJson(Map<String, Object> map);
}