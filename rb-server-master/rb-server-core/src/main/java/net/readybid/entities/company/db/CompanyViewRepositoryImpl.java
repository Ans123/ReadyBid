package net.readybid.entities.company.db;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.company.logic.CompanyViewRepository;
import net.readybid.entities.company.web.CompanySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@Service
public class CompanyViewRepositoryImpl implements CompanyViewRepository {

    private final int pageSize;
    private final ElasticSearchOperations esOperations;
    private final CompanySearchResultViewEsCodec companySearchResultViewEsCodec;

    @Autowired
    public CompanyViewRepositoryImpl(@Value("${elasticsearch.pageSize}") int pageSize, ElasticSearchOperations esOperations) {
        this.pageSize = pageSize;
        this.esOperations = esOperations;

        companySearchResultViewEsCodec = new CompanySearchResultViewEsCodec();
    }

    @Override
    public ListResult<CompanySearchResultView> search(String query, int page) {
        return esOperations.searchEntities(EntityType.COMPANY, query, companySearchResultViewEsCodec, pageSize, page);
    }
}
