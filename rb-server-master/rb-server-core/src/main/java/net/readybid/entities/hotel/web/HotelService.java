package net.readybid.entities.hotel.web;


import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public interface HotelService {
    Hotel findHotelById(String id);

    Hotel findHotelByIdIncludingUnverified(String id);

    ListResult<HotelSearchResultView> searchHotels(String query, int page);
}
