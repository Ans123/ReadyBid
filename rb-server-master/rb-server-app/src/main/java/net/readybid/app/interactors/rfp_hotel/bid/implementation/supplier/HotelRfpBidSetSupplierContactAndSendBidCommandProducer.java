package net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.bidmanagerview.HotelRfpCreateChainRfpBidManagerViewCommand;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;

import java.time.LocalDate;

public interface HotelRfpBidSetSupplierContactAndSendBidCommandProducer {
    HotelRfpBidSetSupplierContactCommand generateSetContactCommand(HotelRfpSupplierContact supplierContact);

    HotelRfpBidSetSupplierContactCommand generateSetContactAndSendBidCommand(HotelRfpSupplierContact supplierContact, HotelRfpBidState bidState, LocalDate sentDate);

    HotelRfpBidSetSupplierContactCommand generateSendBidCommand(HotelRfpBidState bidState, LocalDate sentDate);

    HotelRfpCreateChainRfpBidManagerViewCommand generateCreateChainRfpView();
}
