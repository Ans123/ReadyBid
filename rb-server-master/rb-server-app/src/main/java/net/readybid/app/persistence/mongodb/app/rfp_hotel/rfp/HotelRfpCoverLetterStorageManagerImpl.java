package net.readybid.app.persistence.mongodb.app.rfp_hotel.rfp;

import com.mongodb.client.result.UpdateResult;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepository;
import net.readybid.app.use_cases.rfp_hotel.rfp.gate.HotelRfpCoverLetterStorageManager;
import net.readybid.exceptions.UnableToCompleteRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection.CHAIN_SUPPORT;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection.NAM_COVER_LETTER;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
public class HotelRfpCoverLetterStorageManagerImpl implements HotelRfpCoverLetterStorageManager {

    private final HotelRfpRepository rfpRepository;

    @Autowired
    public HotelRfpCoverLetterStorageManagerImpl(HotelRfpRepository rfpRepository) {
        this.rfpRepository = rfpRepository;
    }

    @Override
    public boolean setNamCoverLetterTemplate(String rfpId, String template) {
        final UpdateResult result = rfpRepository.updateOne(
                and(byId(rfpId), eq(CHAIN_SUPPORT, true)),
                set(NAM_COVER_LETTER, template)
        );
        if(!result.wasAcknowledged())
            throw new UnableToCompleteRequestException(String.format("Failed to save NAM Cover Letter to RFP %s", rfpId));
        return result.getMatchedCount() == 1L;
    }
}
