package net.readybid.core.rfp.hotel.bid;

import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidRequest;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidsResult;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteBidsTransactionImpl implements DeleteBidsTransaction {

    private final BidRepository bidRepository;
    private final HotelRfpBidStateFactory bidStateFactory;

    @Autowired
    public DeleteBidsTransactionImpl(BidRepository bidRepository, HotelRfpBidStateFactory bidStateFactory) {
        this.bidRepository = bidRepository;
        this.bidStateFactory = bidStateFactory;
    }

    @Override
    public DeleteBidsResult delete(DeleteBidRequest request) {
        final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.DELETED, request.currentUser);
        return bidRepository.markAsDeleted(request, state);
    }
}
