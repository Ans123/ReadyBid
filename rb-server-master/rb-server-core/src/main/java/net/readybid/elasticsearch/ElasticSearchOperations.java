package net.readybid.elasticsearch;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.hotel.logic.ElasticSearchModificationSpecification;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 11/1/2016.
 *
 */
public interface ElasticSearchOperations {

    <T> ListResult<T> searchEntities(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page);

    <T> ListResult<T> searchChains(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page);

    <T> ListResult<T> searchHotels(EntityType entityType, String query, EsCodec<T> esCodec, int pageSize, int page);

    void update(ElasticSearchModificationSpecification modificationSpecification);
}
