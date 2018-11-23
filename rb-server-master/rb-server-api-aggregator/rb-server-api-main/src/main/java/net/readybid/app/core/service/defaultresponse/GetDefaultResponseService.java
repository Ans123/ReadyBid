package net.readybid.app.core.service.defaultresponse;

import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;

public interface GetDefaultResponseService {
    HotelRfpDefaultResponse forHotel(String hotelId);
}
