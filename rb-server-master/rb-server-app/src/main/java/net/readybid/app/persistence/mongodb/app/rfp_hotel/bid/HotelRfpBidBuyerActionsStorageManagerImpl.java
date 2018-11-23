package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidBuyerActionsStorageManager;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.STATE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.STATE_STATUS;
import static net.readybid.mongodb.RbMongoFilters.byIds;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.CREATED;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.DELETED;

@Service
public class HotelRfpBidBuyerActionsStorageManagerImpl implements HotelRfpBidBuyerActionsStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidBuyerActionsStorageManagerImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteBids(List<String> bidsIds, HotelRfpBidState bidState) {
        final Bson filter = and( byIds(bidsIds), in(STATE_STATUS, CREATED, DELETED));
        final Bson update = combine(set(STATE, bidState));

        repository.update(filter, update);
    }
}
