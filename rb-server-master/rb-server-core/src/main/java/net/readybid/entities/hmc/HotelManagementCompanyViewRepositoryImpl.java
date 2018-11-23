package net.readybid.entities.hmc;

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
public class HotelManagementCompanyViewRepositoryImpl implements HotelManagementCompanyViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final HotelManagementCompanySearchResultViewEsCodec hmcViewEsCodec;


    @Autowired
    public HotelManagementCompanyViewRepositoryImpl(
            @Value("${elasticsearch.pageSize}") int pageSize,
            ElasticSearchOperations esOperations
    ) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;
        hmcViewEsCodec = new HotelManagementCompanySearchResultViewEsCodec();
    }


    @Override
    public ListResult<HotelManagementCompanyFacadeSearchResultView> search(String query, int page) {
        return esOperations.searchEntities(EntityType.HMC, query, hmcViewEsCodec, pageSize, page);
    }
}
