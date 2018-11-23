package net.readybid.app.interactors.rfp_hotel.dirty;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.auth.account.core.Account;
import net.readybid.rfphotel.supplier.RfpHotel;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public interface RfpHotelFactory {
    RfpHotel create(HotelRfpBidBuilder hotelRfpBidBuilder);
    RfpHotel create(Hotel hotel, Account account);
}
