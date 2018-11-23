package net.readybid.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.readybid.utils.ListResult;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 11/2/2016.
 *
 */
public class ElasticSearchConverterImpl implements ElasticSearchConverter {

    private ObjectMapper objectMapper;

    public ElasticSearchConverterImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Deprecated
    public <T> ElasticSearchResult<T> convert(Response response, Class<T> tClass){
        try {
            ElasticSearchResult<T> results = new ElasticSearchResult<T>();
            Map responseMap = objectMapper.readValue(response.getEntity().getContent(), Map.class);
            parseResponseMap(results, responseMap, tClass);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> ListResult<T> convert(Response response, EsCodec<T> codec) {
        try {
            final Map<?, ?> responseMap = objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return parseResponseMap(responseMap, codec);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    private <T> void parseResponseMap(ElasticSearchResult<T> results, Map responseMap, Class<T> tClass) throws InvalidElasticSearchResultException, IOException {
        if (responseMap == null) throw new InvalidElasticSearchResultException();

        results.took = Long.parseLong(responseMap.getOrDefault("took", "-1").toString());
        results.timedOut = Boolean.parseBoolean(responseMap.getOrDefault("timed_out", "false").toString());

        parseHits(results, (Map) responseMap.get("hits"), tClass);
    }

    private <T> ListResult<T> parseResponseMap(Map<?, ?> responseMap, EsCodec<T> codec) {
        if (responseMap == null) throw new InvalidElasticSearchResultException();
        return parseHits((Map) responseMap.get("hits"), codec);
    }

    @Deprecated
    private <T> void parseHits(ElasticSearchResult<T> results, Map hits, Class<T> tClass) throws InvalidElasticSearchResultException, IOException {
        if (hits == null) throw new InvalidElasticSearchResultException();
        results.total = Long.parseLong(hits.getOrDefault("total", "0").toString());
        parseHitsList(results, (List<Map>) hits.get("hits"), tClass);
    }

    private <T> ListResult<T> parseHits(Map<?, ?> hits, EsCodec<T> codec) {
        if (hits == null) throw new InvalidElasticSearchResultException();

        final long total = hits.containsKey("total") ? Long.parseLong(hits.get("total").toString()) : 0;

        //noinspection unchecked
        final List<T> data = parseHitsList((List<Map>) hits.get("hits"), codec);

        return new ListResult<T>(data, total);
    }

    private <T> List<T> parseHitsList(List<Map> hits, EsCodec<T> codec) {
        final List<T> items = new ArrayList<>();
        for (Map hit : hits) {
            items.add(codec.decode((Map) hit.get("_source")));
        }

        return items;
    }

    @Deprecated
    private <T> void parseHitsList(ElasticSearchResult<T> results, List<Map> hits, Class<T> tClass) throws IOException {
        results.items = new ArrayList<>();
        for (Map hit : hits) {
            results.items.add(objectMapper.readValue(objectMapper.writeValueAsString(hit.get("_source")), tClass));
        }
    }


    private class InvalidElasticSearchResultException extends RuntimeException {
    }
}
