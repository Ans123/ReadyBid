package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.user.BasicUserDetails;

import java.util.List;

public interface HotelRfpBidSupplierContactService {
    void set(List<String> bidsIds, HotelRfpSupplierContact supplierContact);

    void setContactAndSendBids(List<String> bidsIds, HotelRfpSupplierContact supplierContact, BasicUserDetails currentUser);

    void sendBids(List<String> bidsIds, BasicUserDetails currentUser);
}
