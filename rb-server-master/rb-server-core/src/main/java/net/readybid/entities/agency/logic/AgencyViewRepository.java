package net.readybid.entities.agency.logic;

import net.readybid.entities.agency.web.AgencySearchResultView;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface AgencyViewRepository {
    ListResult<AgencySearchResultView> search(String query, int page);
}
