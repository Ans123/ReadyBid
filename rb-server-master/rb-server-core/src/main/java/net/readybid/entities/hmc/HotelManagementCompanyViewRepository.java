package net.readybid.entities.hmc;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public interface HotelManagementCompanyViewRepository {
    ListResult<HotelManagementCompanyFacadeSearchResultView> search(String query, int page);
}
