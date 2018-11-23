package net.readybid.entities.company.web;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */

public interface CompanyFacade {
    ListResult<CompanySearchResultView> searchCompanies(String query, int page);

}
