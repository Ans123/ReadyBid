package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactCommand;

import java.util.List;

public interface HotelRfpBidSupplierContactStorageManager {
    void setContactAndSendBid(List<HotelRfpBidSetSupplierContactCommand> commands);
}
