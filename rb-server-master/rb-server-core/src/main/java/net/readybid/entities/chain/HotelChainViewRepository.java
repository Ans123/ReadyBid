package net.readybid.entities.chain;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 2/15/2017.
 */
public interface HotelChainViewRepository {
    ListResult<HotelChainSearchResultView> search(String query, int page);
}
