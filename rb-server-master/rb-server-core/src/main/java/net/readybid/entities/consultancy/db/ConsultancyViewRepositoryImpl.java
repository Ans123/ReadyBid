package net.readybid.entities.consultancy.db;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.consultancy.logic.ConsultancyViewRepository;
import net.readybid.entities.consultancy.web.ConsultancySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@Service
public class ConsultancyViewRepositoryImpl implements ConsultancyViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final ConsultancySearchResultViewEsCodec consultancySearchResultViewEsCodec;

    @Autowired
    public ConsultancyViewRepositoryImpl(@Value("${elasticsearch.pageSize}") int pageSize, ElasticSearchOperations esOperations) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;

        consultancySearchResultViewEsCodec = new ConsultancySearchResultViewEsCodec ();
    }

    @Override
    public ListResult<ConsultancySearchResultView> search(String query, int page) {
        return esOperations.searchEntities(EntityType.TRAVEL_CONSULTANCY, query, consultancySearchResultViewEsCodec, pageSize, page);
    }
}
