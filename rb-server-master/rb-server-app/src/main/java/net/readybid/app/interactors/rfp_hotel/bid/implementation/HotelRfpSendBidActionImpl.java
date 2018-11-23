package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactService;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpSendBidAction;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidLoader;
import net.readybid.exceptions.BadRequestException;
import net.readybid.user.BasicUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelRfpSendBidActionImpl implements HotelRfpSendBidAction {

    private final HotelRfpBidLoader bidLoader;
    private final HotelRfpBidSupplierContactService supplierContactService;

    @Autowired
    public HotelRfpSendBidActionImpl(
            HotelRfpBidLoader bidLoader,
            HotelRfpBidSupplierContactService supplierContactService
    ) {
        this.bidLoader = bidLoader;
        this.supplierContactService = supplierContactService;
    }

    @Override
    public void sendBids(List<String> bidsIds, BasicUserDetails currentUser, boolean ignoreDueDate) {
        if(!ignoreDueDate) checkDueDate(bidsIds);
        supplierContactService.sendBids(bidsIds, currentUser);
    }

    @Override
    public void sendBidsToNewContact(List<String> bidsIds, HotelRfpSupplierContact supplierContact, BasicUserDetails currentUser, boolean ignoreDueDate) {
        if(supplierContact == null) return;
        if(!ignoreDueDate) checkDueDate(bidsIds);
        supplierContactService.setContactAndSendBids(bidsIds, supplierContact, currentUser);
    }

    private void checkDueDate(List<String> bidsIds) {
        final LocalDate expiredDueDate = bidLoader.getExpiredBidDueDate(bidsIds);
        if(expiredDueDate != null)
            throw new BadRequestException("DUE_DATE_EXPIRED", "Due Date has expired", String.valueOf(expiredDueDate));
    }
}
