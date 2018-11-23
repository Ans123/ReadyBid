package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid.response;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.SetBidReceivedData;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byState;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.RECEIVED;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.SENT;

@Service
public class HotelRfpBidResponseStorageManagerImpl implements HotelRfpBidResponseStorageManager {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidResponseStorageManagerImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveDraftResponses(List<HotelRfpBid> responses) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

        for(final HotelRfpBid bid: responses){
            writes.add(new UpdateOneModel<>(
                    and(byId(bid.getId()), byState(SENT, RECEIVED)),
                    setDraftAndState(bid.getState(), bid.getResponseDraft())));
        }

        if(!writes.isEmpty()) repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void setBidsAsReceived(List<SetBidReceivedData> data) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

        for(final SetBidReceivedData b: data){
            if(b != null) writes.add(new UpdateOneModel<>(
                    and(byId(b.bidId), byState(SENT)),
                    setDraftAndState(b.receivedState, b.response)
                    ));
        }
        if(!writes.isEmpty()) repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    @Override
    public void saveResponses(List<HotelRfpBid> responses) {
        final List<WriteModel<HotelRfpBidImpl>> writes = new ArrayList<>();

        for(final HotelRfpBid bid: responses){
            if(bid != null) writes.add(new UpdateOneModel<>(
                    and(byId(bid.getId()), byState(RECEIVED)),
                    combine(
                            set(QUESTIONNAIRE_RESPONSE, bid.getResponse()),
                            unset(QUESTIONNAIRE_RESPONSE_DRAFT),
                            set(NEGOTIATIONS, bid.getNegotiations()),
                            set(OFFER, bid.getOffer()),
                            set(ANALYTICS, bid.getAnalytics()),
                            set(SUPPLIER_CONTACT, bid.getSupplierContact()),
                            set(STATE, bid.getState())
                    )));
        }
        if(!writes.isEmpty()) repository.bulkWrite(writes, new BulkWriteOptions().ordered(false));
    }

    private Bson setDraftAndState(HotelRfpBidState receivedState, QuestionnaireResponse response) {
        return combine(
                set(QUESTIONNAIRE_RESPONSE_DRAFT, response),
                set(STATE, receivedState)
        );
    }
}
