package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import com.mongodb.client.result.UpdateResult;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.use_cases.rfp_hotel.bid.gate.HotelRfpBidCoverLetterStorageManager;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.rfp.specifications.HotelRfpType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.oid;

@Service
public class HotelRfpBidCoverLetterStorageManagerImpl implements HotelRfpBidCoverLetterStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidCoverLetterStorageManagerImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateNamCoverLetterTemplateAsBidCoverLetter(String rfpId, String template) {
        final UpdateResult result = repository.update(
                and(
                        eq(RFP_ID, oid(rfpId)),
                        eq(BID_HOTEL_RFP_TYPE, HotelRfpType.CHAIN)
                ),
                set(COVER_LETTER, template));
        if(!result.wasAcknowledged())
            throw new UnableToCompleteRequestException(String.format("Failed to save NAM Cover Letter to Bids from RFP %s", rfpId));
    }
}
