package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid.response;

import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byIds;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.RECEIVED;

@Service
public class HotelRfpBidResponseLoaderImpl implements HotelRfpBidResponseLoader {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidResponseLoaderImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public HotelRfpBid getBidWithQuestionnaireHotelIdResponseContextFields(String bidId) {
        return repository.findOne(
                byId(bidId),
                include(QUESTIONNAIRE, PROGRAM_START_DATE, PROGRAM_END_DATE, STATE));
    }

    @Override
    public List<HotelRfpBid> getBidsWithQuestionnaireHotelIdResponseContextFields(List<String> bidsIds) {
        final List<? extends HotelRfpBid> bids = repository.find(
                and(byIds(bidsIds)),
                include(SUPPLIER_COMPANY_ENTITY_ID, QUESTIONNAIRE, PROGRAM_START_DATE, PROGRAM_END_DATE, STATE));

        return new ArrayList<>(bids);
    }

    @Override
    public List<HotelRfpBid> getBidsWithQuestionnaireHotelIdResponseContextFieldsByRfpAndSupplierContact(ObjectId rfpId, String emailAddress) {
        final List<? extends HotelRfpBid> bids = repository.find(
                and(eq(RFP_ID, rfpId), eq(SUPPLIER_CONTACT_EMAIL_ADDRESS, emailAddress)),
                include(SUPPLIER_COMPANY_ENTITY_ID, QUESTIONNAIRE, PROGRAM_START_DATE, PROGRAM_END_DATE, STATE));

        return new ArrayList<>(bids);
    }

    @Override
    public List<HotelRfpBid> loadBidsWithDraftToResponseContext(List<String> bidsIds) {
        final List<? extends HotelRfpBid> bids = repository.find(
                and(byIds(bidsIds), in(STATE_STATUS, RECEIVED)),
                include(QUESTIONNAIRE_RESPONSE_DRAFT, ANALYTICS, SUPPLIER_COMPANY_ENTITY_ID));
        return new ArrayList<>(bids);
    }

    @Override
    public HotelRfpBid loadBidWithSetResponseContext(String bidId) {
        return repository.findOne(
                and(byId(bidId), in(STATE_STATUS, RECEIVED)),
                include(QUESTIONNAIRE, PROGRAM_START_DATE, PROGRAM_END_DATE, ANALYTICS, SUPPLIER_COMPANY_ENTITY_ID, BUYER_COMPANY_NAME));
    }
}
