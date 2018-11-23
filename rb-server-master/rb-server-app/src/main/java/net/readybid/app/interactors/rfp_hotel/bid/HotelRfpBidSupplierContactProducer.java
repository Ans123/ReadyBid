package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.user.BasicUserDetails;

import java.util.List;

public interface HotelRfpBidSupplierContactProducer {
//    HotelRfpSupplierContact create(String bidId, CreateHotelRfpSupplierContactRequest request,
//                                   BasicUserDetails currentUser);

    HotelRfpSupplierContact create(List<String> bidsIds, String userAccountId);

    HotelRfpSupplierContact create(List<String> bidsIds, CreateHotelRfpSupplierContactRequest request,
                                   BasicUserDetails currentUser);
}
