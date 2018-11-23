package net.readybid.entities.company.logic;

import net.readybid.entities.company.web.CompanySearchResultView;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface CompanyViewRepository {

    ListResult<CompanySearchResultView> search(String query, int page);
}
