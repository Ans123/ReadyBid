package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpRateLoadingInformationCollection;
import net.readybid.mongodb.RbMongoFilters;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.*;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.DELETED;

@Service
public class HotelRfpBidLoaderImpl implements HotelRfpBidLoader {

    private final HotelRfpBidRepository repository;

    @Autowired
    public HotelRfpBidLoaderImpl(HotelRfpBidRepository repository) {
        this.repository = repository;
    }

    @Override
    public LocalDate getExpiredBidDueDate(List<String> bidsIds) {
        return repository.find(
                and( byIds(bidsIds), lt(DUE_DATE, LocalDate.now())),
                include(DUE_DATE))
                .stream().findAny().map(HotelRfpBid::getDueDate).orElse(null);
    }

    @Override
    public List<HotelRfpBid> listTravelDestinationProperties(String rfpId, String destinationId) {
        final List<? extends HotelRfpBid> bids = repository.find(
                and(eq(RFP_ID, oid(rfpId)), eq(SUBJECT_ID, oid(destinationId)), nin(STATE_STATUS, DELETED)),
                include(SUPPLIER_COMPANY_NAME, SUPPLIER_COMPANY_FULL_ADDRESS, DISTANCE_MI, DISTANCE_KM));
        return new ArrayList<>(bids);
    }

    @Override
    public List<HotelRfpBid> getResponseAnswers(List<String> bidsIds) {
        final List<? extends HotelRfpBid> bids = repository.find(
                and(byIds(bidsIds), nin(STATE_STATUS, DELETED)),
                include(QUESTIONNAIRE_RESPONSE_ANSWERS));

        sortBids(bidsIds, bids);

        return new ArrayList<>(bids);
    }

    @Override
    public List<HotelRfpBid> getFinalAgreements(List<String> bidsIds, boolean includeUnsent) {
        final Bson stateFilter = includeUnsent
                ? byState(HotelRfpBidStateStatus.FINAL_AGREEMENT, HotelRfpBidStateStatus.RESPONDED, HotelRfpBidStateStatus.NEGOTIATION_FINALIZED )
                : byState(HotelRfpBidStateStatus.FINAL_AGREEMENT);
        final List<Bson> pipeline = Arrays.asList(
                match(and(byIds(bidsIds), stateFilter)),
                project(RbMongoFilters.include(
                        FINAL_AGREEMENT,
                        QUESTIONNAIRE,
                        BUYER_COMPANY_ENTITY_ID,
                        BUYER_COMPANY_NAME,
                        BUYER_COMPANY_ACCOUNT_ID,
                        FINAL_AGREEMENT_DATE)),
                lookup(
                        HotelRfpRateLoadingInformationCollection.COLLECTION_NAME,
                        BUYER_COMPANY_ENTITY_ID,
                        HotelRfpRateLoadingInformationCollection.ENTITY_ID,
                        RATE_LOADING_INFORMATION),
                unwind("$"+RATE_LOADING_INFORMATION, true),
                addFields(new Document(RATE_LOADING_INFORMATION, "$rateLoadingInformation.information"))

        );

        List<? extends HotelRfpBid> bids = repository.aggregate(pipeline);
        sortBids(bidsIds, bids);
        return new ArrayList<>(bids);
    }

    private static void sortBids(List<String> bidsIds, List<? extends HotelRfpBid> bids) {
        bids.sort((a,b) -> {
            final int aIndex = bidsIds.indexOf(String.valueOf(a.getId()));
            final int bIndex = bidsIds.indexOf(String.valueOf(b.getId()));
            return aIndex - bIndex;
        });
    }
}
