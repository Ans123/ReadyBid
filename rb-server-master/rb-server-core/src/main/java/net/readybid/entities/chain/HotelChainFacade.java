package net.readybid.entities.chain;


import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public interface HotelChainFacade {
    ListResult<HotelChainListItemViewModel> listAllChains();

    ListResult<HotelChainSearchResultView> searchChains(String query, int page);

    HotelChain findHotelByIdIncludingUnverified(String id);

    HotelChain findHotelById(String id);
}
