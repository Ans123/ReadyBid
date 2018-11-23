package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.entities.Id;

public interface HotelRfpDefaultResponseLoader {
    HotelRfpDefaultResponse get(Id hotelId);
}
