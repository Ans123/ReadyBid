package net.readybid.entities.agency.db;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.agency.logic.AgencyViewRepository;
import net.readybid.entities.agency.web.AgencySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@Service
public class AgencyViewRepositoryImpl implements AgencyViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final AgencySearchResultViewEsCodec agencySearchResultViewEsCodec;

    @Autowired
    public AgencyViewRepositoryImpl(@Value("${elasticsearch.pageSize}") int pageSize, ElasticSearchOperations esOperations) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;

        agencySearchResultViewEsCodec = new AgencySearchResultViewEsCodec();
    }

    @Override
    public ListResult<AgencySearchResultView> search(String query, int page) {
        return esOperations.searchEntities(EntityType.TRAVEL_AGENCY, query, agencySearchResultViewEsCodec, pageSize, page);
    }
}
