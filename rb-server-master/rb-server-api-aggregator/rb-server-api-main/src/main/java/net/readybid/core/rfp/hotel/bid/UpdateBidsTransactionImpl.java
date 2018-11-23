package net.readybid.core.rfp.hotel.bid;

import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateBidsTransactionImpl implements UpdateBidsTransaction {

    private final BidRepository bidRepository;

    @Autowired
    public UpdateBidsTransactionImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public void updateTravelDestination(UpdateBidTravelDestinationRequest request) {
        bidRepository.updateTravelDestination(request.destination);
        System.out.println("hasDestinationCoordinatesChanged = " + request.hasDestinationCoordinatesChanged);
        if(request.hasDestinationCoordinatesChanged){
            bidRepository.updateDistancesForDestination(request.destinationId);
        }
    }
}
