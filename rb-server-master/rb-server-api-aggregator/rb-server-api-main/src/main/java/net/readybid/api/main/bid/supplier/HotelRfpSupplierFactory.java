package net.readybid.api.main.bid.supplier;

import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public interface HotelRfpSupplierFactory {

    HotelRfpSupplier create(HotelRfpBidBuilder hotelRfpBidBuilder);
}
