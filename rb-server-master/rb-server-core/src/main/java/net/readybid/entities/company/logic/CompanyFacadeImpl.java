package net.readybid.entities.company.logic;


import net.readybid.entities.company.web.CompanyFacade;
import net.readybid.entities.company.web.CompanySearchResultView;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@Service
public class CompanyFacadeImpl implements CompanyFacade {

    private final CompanyViewRepository companyViewRepository;

    @Autowired
    public CompanyFacadeImpl(CompanyViewRepository companyViewRepository) {
        this.companyViewRepository = companyViewRepository;
    }


    @Override
    public ListResult<CompanySearchResultView> searchCompanies(String query, int page) {
        return companyViewRepository.search(query, page);
    }

}
