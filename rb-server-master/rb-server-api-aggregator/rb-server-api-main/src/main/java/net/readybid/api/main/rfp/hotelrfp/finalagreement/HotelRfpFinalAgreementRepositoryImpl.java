package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.readybid.api.main.entity.rateloadinginformation.RateLoadingInformationRepository;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.mongodb.MongoRetry;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpImpl;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class HotelRfpFinalAgreementRepositoryImpl implements HotelRfpFinalAgreementRepository {

    private final MongoCollection<HotelRfpBidImpl> bidCollection;
    private final MongoCollection<RfpImpl> rfpCollection;

    @Autowired
    public HotelRfpFinalAgreementRepositoryImpl(MongoDatabase mongoDatabase) {
        bidCollection = mongoDatabase.getCollection(COLLECTION_NAME, HotelRfpBidImpl.class);
        rfpCollection = mongoDatabase.getCollection(HotelRfpCollection.COLLECTION_NAME, RfpImpl.class);
    }

    @Override
    @MongoRetry
    public HotelRfpBid getFinalAgreementWithModelData(String bidId) {
        return bidCollection.aggregate(Arrays.asList(
                match(new Document(ID, oid(bidId))),
                project(include(
                        FINAL_AGREEMENT,
                        RFP_ID,
                        RFP_SPECIFICATIONS,
                        QUESTIONNAIRE,
                        SUBJECT,
                        BUYER,
                        SUPPLIER,
                        FINAL_AGREEMENT_DATE)),
                lookup(
                        RateLoadingInformationRepository.COLLECTION,
                        BUYER_COMPANY_ENTITY_ID,
                        "entityId",
                        RATE_LOADING_INFORMATION),
                unwind("$"+RATE_LOADING_INFORMATION, true),
                addFields(new Document(RATE_LOADING_INFORMATION, "$rateLoadingInformation.information"))
        )).first();
    }

    @Override
    @MongoRetry
    public HotelRfpBid send(HotelRfpBid bid, List<? extends RateLoadingInformation> information) {
        final HotelRfpBidImpl updatedBid =  bidCollection.findOneAndUpdate(
                and(
                        byId(bid.getId()),
                        byState(HotelRfpBidStateStatus.RESPONDED, HotelRfpBidStateStatus.NEGOTIATION_FINALIZED)
                ),
                combine(
                        set(STATE, bid.getState()),
                        set(FINAL_AGREEMENT_DATE, bid.getFinalAgreementDate())
                ),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );
        if(updatedBid != null){
            updatedBid.setRateLoadingInformation(information);
        }
        return updatedBid;
    }

    @Override
    @MongoRetry
    public String getTemplateFromRfp(String rfpId) {
        final RfpImpl rfp = rfpCollection.find(byId(rfpId)).projection(include(HotelRfpCollection.FINAL_AGREEMENT)).first();
        return rfp == null ? null : rfp.getFinalAgreementTemplate();
    }

    @Override
    @MongoRetry
    public Rfp updateTemplateInRfpIfNotInStates(String rfpId, String sanitizedTemplate, List<RfpStatus> states) {
        return rfpCollection.findOneAndUpdate(
                and(byId(rfpId), not(in(HotelRfpCollection.STATUS_VALUE, states))),
                set(HotelRfpCollection.FINAL_AGREEMENT, sanitizedTemplate),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }

    @Override
    @MongoRetry
    public void updateTemplateInSyncedRfpBids(String rfpId, String sanitizedTemplate) {
        bidCollection.updateMany(
                and(eq(RFP_ID, oid(rfpId)), exists(NO_SYNC, false)),
                set(FINAL_AGREEMENT, sanitizedTemplate));
    }

    @Override
    @MongoRetry
    public String getTemplateFromBid(String bidId) {
        final HotelRfpBidImpl bid = bidCollection.find(byId(bidId)).projection(include(FINAL_AGREEMENT)).first();
        return bid == null ? null : bid.getFinalAgreementTemplate();
    }

    @Override
    @MongoRetry
    public void updateTemplateInBidIfNotInStates(String bidId, String sanitizedTemplate, List<HotelRfpBidStateStatus> states) {
        bidCollection.updateOne(
                and(byId(bidId), not(in(STATE_STATUS, states))),
                combine(set(FINAL_AGREEMENT, sanitizedTemplate), set(NO_SYNC, true)));
    }
}
