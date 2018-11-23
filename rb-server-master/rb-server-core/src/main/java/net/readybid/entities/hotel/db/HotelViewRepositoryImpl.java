package net.readybid.entities.hotel.db;

import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.hotel.logic.HotelViewRepository;
import net.readybid.entities.hotel.web.HotelSearchResultView;
import net.readybid.mongodb.RetryAsIdempotent;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
@Service
public class HotelViewRepositoryImpl implements HotelViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final HotelSearchResultViewEsCodec hotelSearchResultViewEsCodec;

    @Autowired
    public HotelViewRepositoryImpl(
            @Value("${elasticsearch.pageSize}") int pageSize,
            ElasticSearchOperations esOperations,
            @Qualifier("mongoDatabaseMain") MongoDatabase defaultMongoDatabase
    ) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;
        hotelSearchResultViewEsCodec = new HotelSearchResultViewEsCodec();
    }

    @Override
    @RetryAsIdempotent
    public ListResult<HotelSearchResultView> search(String query, int page) {
        return esOperations.searchHotels(EntityType.HOTEL, query, hotelSearchResultViewEsCodec, pageSize, page);
    }
}
