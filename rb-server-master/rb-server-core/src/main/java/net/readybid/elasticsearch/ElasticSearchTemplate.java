package net.readybid.elasticsearch;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.hotel.logic.ElasticSearchModificationSpecification;
import net.readybid.exceptions.UnrecoverableException;
import net.readybid.utils.ListResult;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public class ElasticSearchTemplate implements ElasticSearchOperations {

    private final ElasticSearchClient client;
    private final ElasticSearchConverter converter;

    public ElasticSearchTemplate(
            ElasticSearchFactory factory,
            ElasticSearchConverter converter
    ) {
        this.converter = converter;
        client = factory.createClient();
    }

    @Override
    public <T> ListResult<T> searchEntities(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page) {
        return searchWithTemplate("searchEntities", entityType, query, esCodec, pageSize, page);
    }

    @Override
    public <T> ListResult<T> searchChains(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page) {
        return searchWithTemplate("searchChains", entityType, query, esCodec, pageSize, page);
    }

    @Override
    public <T> ListResult<T> searchHotels(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page) {
        return searchWithTemplate("searchHotels", entityType, query, esCodec, pageSize, page);
    }

    @Override
    public void update(ElasticSearchModificationSpecification modificationSpecification) {
        final String type = modificationSpecification.getIndex();
        final String path = modificationSpecification.getPath();
        final Map<String, String> params = modificationSpecification.getParams();
        final HttpEntity body = new NStringEntity(modificationSpecification.getBody(), ContentType.APPLICATION_JSON);

        try {
            client.post(type, path, params, body);
        } catch (IOException e) {
            throw new UnrecoverableException(e);
        }
    }

    private <T> ListResult<T> searchWithTemplate(String template, EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page) {
        try {
            final String TEMPLATE_SEARCH_FORMAT = "{ \"id\": \"%s\", \"params\": { \"query\": \"%s\", \"from\": %s, \"size\": %s } }";

            final HttpEntity body = new NStringEntity(String.format(TEMPLATE_SEARCH_FORMAT, template, query, pageSize * (page-1), pageSize), ContentType.APPLICATION_JSON);
            final Response response = client.get(entityType.toElasticSearchType(), body);
            return converter.convert(response, esCodec);
        } catch (IOException e) {
            throw new UnrecoverableException(e);
        }
    }
}