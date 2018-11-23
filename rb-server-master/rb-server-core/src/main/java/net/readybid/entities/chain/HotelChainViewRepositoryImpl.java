package net.readybid.entities.chain;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
@Service
public class HotelChainViewRepositoryImpl implements HotelChainViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final ChainSearchResultViewEsCodec chainSearchResultViewEsCodec;

    @Autowired
    public HotelChainViewRepositoryImpl(
            @Value("${elasticsearch.pageSize}") int pageSize,
            ElasticSearchOperations esOperations
    ) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;
        chainSearchResultViewEsCodec = new ChainSearchResultViewEsCodec();
    }

    @Override
    public ListResult<HotelChainSearchResultView> search(String query, int page) {
        return esOperations.searchChains(EntityType.CHAIN, query, chainSearchResultViewEsCodec, pageSize, page);
    }
}
