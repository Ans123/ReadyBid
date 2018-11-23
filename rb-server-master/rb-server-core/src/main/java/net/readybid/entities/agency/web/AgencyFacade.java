package net.readybid.entities.agency.web;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public interface AgencyFacade {
    ListResult<AgencySearchResultView> searchAgencies(String query, int page);
}
