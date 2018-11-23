package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidQueryViewLoader;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidQueryViewRepository;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection.*;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class HotelRfpBidQueryViewLoaderImpl implements HotelRfpBidQueryViewLoader {

    private final HotelRfpBidQueryFactory bidQueryFactory;
    private final HotelRfpBidQueryViewRepository bidRepository;

    @Autowired
    public HotelRfpBidQueryViewLoaderImpl(
            HotelRfpBidQueryViewRepository bidRepository
    ) {
        this.bidQueryFactory = new HotelRfpBidQueryFactory();
        this.bidRepository = bidRepository;
    }

    @Override
    public List<HotelRfpBidQueryView> find(List<Id> ids, AuthenticatedUser currentUser) {
        final Bson projection = include(ID, RFP_ID, RFP_SPECIFICATIONS, SUBJECT, BUYER, SUPPLIER, ANALYTICS, OFFER, STATE, QUESTIONNAIRE_RESPONSE_ANSWERS);
        return findAll(byId(ids), projection, currentUser);
    }

    @Override
    public List<HotelRfpBidQueryView> query(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user) {
        final HotelRfpBidManagerViewQuery query = bidQueryFactory.createQuery(bidsQueryRequest, user);
        return findAll(query.filter, query.projection, user);
    }

    @Override
    public List<HotelRfpBidQueryView> query(List<String> bidsIds, List<String> fields, AuthenticatedUser currentUser) {
        final List<HotelRfpBidQueryView> bids = findAll(byIds(bidsIds), bidQueryFactory.createProjection(fields), currentUser);
        sortBids(bidsIds, bids);
        return bids;
    }

    private static void sortBids(List<String> bidsIds, List<HotelRfpBidQueryView> bids) {
        bids.sort((a,b) -> {
            final int aIndex = bidsIds.indexOf(String.valueOf(a.$bidId));
            final int bIndex = bidsIds.indexOf(String.valueOf(b.$bidId));
            return aIndex - bIndex;
        });
    }

    private List<HotelRfpBidQueryView> findAll(Bson filter, Bson projection, AuthenticatedUser currentUser) {
        return bidRepository.findAll(filter, projection)
                .stream()
                .map(updateStatusLabels(currentUser))
                .collect(Collectors.toList());
    }

    private static Function<HotelRfpBidQueryView.Builder, HotelRfpBidQueryView> updateStatusLabels(AuthenticatedUser currentUser) {
        return r -> RfpType.HOTEL.isSupplier(currentUser.getAccountType()) ? r.buildSupplierView() : r.buildBuyerView();
    }
}
