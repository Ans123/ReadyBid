package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import com.mongodb.client.result.UpdateResult;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
class HotelRfpBidStorageManagerImpl implements HotelRfpBidStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    HotelRfpBidStorageManagerImpl(
            HotelRfpBidRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void enableChainSupport(String rfpId) {
        final UpdateResult result = repository.update(
                eq(HotelRfpBidCollection.RFP_ID, oid(rfpId)),
                set(HotelRfpBidCollection.RFP_CHAIN_SUPPORT, true)
        );
        if(!result.wasAcknowledged())
            throw new UnableToCompleteRequestException(String.format("EnableChainSupport Failed for all bids from RFP: %s", rfpId));
    }

    @Override
    public void setAcceptedRates(String bidId, HotelRfpAcceptedRates hotelRfpAcceptedRates) {
        final UpdateResult result = repository.update(
                and(byId(bidId), byState(HotelRfpBidStateStatus.RESPONDED, HotelRfpBidStateStatus.NEGOTIATION_FINALIZED)),
                set(HotelRfpBidCollection.ACCEPTED_RATES, hotelRfpAcceptedRates)
        );
        if(!result.wasAcknowledged())
            throw new UnableToCompleteRequestException(String.format("Set Accepted Rate Failed for: %s", bidId));
        if(result.getMatchedCount() == 0){
            throw new NotFoundException();
        }
    }
}
