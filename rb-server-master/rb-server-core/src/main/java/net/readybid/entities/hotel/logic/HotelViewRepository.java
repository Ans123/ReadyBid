package net.readybid.entities.hotel.logic;


import net.readybid.entities.hotel.web.HotelSearchResultView;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public interface HotelViewRepository {

    ListResult<HotelSearchResultView> search(String query, int page);
}
