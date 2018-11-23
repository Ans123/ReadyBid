package net.readybid.app.use_cases.rfp_hotel;

import net.readybid.web.RbViewModel;

public interface ListHotelRfpRepresentatives {
    RbViewModel forChain(String chainAccountId);

    RbViewModel forHotel(String hotelAccountId);
}
