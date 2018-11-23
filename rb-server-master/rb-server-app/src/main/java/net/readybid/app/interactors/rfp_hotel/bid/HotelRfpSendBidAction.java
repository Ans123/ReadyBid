package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.user.BasicUserDetails;

import java.util.List;

public interface HotelRfpSendBidAction {
    void sendBids(List<String> bidsIds, BasicUserDetails currentUser, boolean ignoreDueDate);

    void sendBidsToNewContact(List<String> bidsIds, HotelRfpSupplierContact supplierContact, BasicUserDetails currentUser, boolean ignoreDueDate);
}
