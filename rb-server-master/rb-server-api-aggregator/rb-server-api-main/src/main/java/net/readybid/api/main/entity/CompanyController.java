package net.readybid.api.main.entity;

import net.readybid.entities.company.web.CompanyFacade;
import net.readybid.entities.company.web.CompanySearchResultView;
import net.readybid.utils.ListResult;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
@RestController
@RequestMapping(value = "/companies")
public class CompanyController {

    private final CompanyFacade companyFacade;

    @Autowired
    public CompanyController(CompanyFacade companyFacade) {
        this.companyFacade = companyFacade;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ListResponse<CompanySearchResultView> searchEntities(
            @RequestParam(value = "query", required = true, defaultValue = "") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        ListResponse<CompanySearchResultView> sr = new ListResponse<>();
        ListResult<CompanySearchResultView> lr = companyFacade.searchCompanies(query, page);
        return sr.finalizeResult(lr);
    }
}
