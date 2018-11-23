package net.readybid.bidmanagerview;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.Id;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.type.RfpType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
@Service
public class BidManagerViewFactoryImpl implements BidManagerViewFactory {

    @Override
    public BidManagerView createBuyerHotelRfpView(String viewName, Rfp rfp, ObjectId ownerAccountId) {
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(rfp.getId());
        view.setRfpType(RfpType.HOTEL);
        view.setType(BidManagerViewType.RFP);
        view.setRfpId(rfp.getId());
        view.setSide(BidManagerViewSide.BUYER);
        view.setName(viewName);
        view.setColumns(Arrays.asList("hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country", "bid.status", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.chainName", "hotelRfpBid.hotel.brandName", "hotelRfpBid.hotel.rating", "hotelRfpBid.td.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.contactName", "bid.currency", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.dyn", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.ia", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score"));
        view.setFilter(createFilterForHotelRfp(rfp.getId()));
        view.setSort(sortByStatus());
        view.setGroup("hotelRfpBid.td.name");
        view.setOwner(ownerAccountId);

        return view;
    }

    @Override
    public BidManagerView createChainHotelRfpView(String viewName, Id rfpId, Id ownerAccountId) {
        final ObjectId rfpObjectId = new ObjectId(String.valueOf(rfpId));
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(new ObjectId());
        view.setRfpType(RfpType.HOTEL);
        view.setType(BidManagerViewType.RFP);
        view.setRfpId(rfpObjectId);
        view.setSide(BidManagerViewSide.SUPPLIER);
        view.setName(viewName);
        view.setColumns(Arrays.asList("hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country", "bid.status", "bid.issuer.companyName", "bid.issuer.contactName", "hotelRfpBid.td.address", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.dyn", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.ia", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.scoreStanding"));
        view.setFilter(createFilterForHotelRfp(rfpObjectId));
        view.setSort(sortByStatus());
        view.setGroup("hotelRfpBid.hotel.city");
        view.setOwner(new ObjectId(String.valueOf(ownerAccountId)));

        return view;
    }

    @Override
    public String createNameForRfpView(String rfpName) {
        return String.format("%s View", rfpName);
    }

    private Map<String, Object> sortByStatus() {
        final Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("bid.status", -1);
        return sortMap;
    }

    public BidManagerView createDefaultView(EntityType userAccountType, ObjectId ownerAccountId) {
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(new ObjectId());
        view.setRfpType(RfpType.HOTEL);
        view.setType(BidManagerViewType.ALL);
        view.setName("All Hotel RFP Bids");

        view.setSide(BidManagerViewSide.determineSide(RfpType.HOTEL, userAccountType));
        view.setColumns(getAllHotelRfpBidViewColumnsForUserType(userAccountType));

        view.setFilter(createFilterForRfpType(RfpType.HOTEL));
        view.setSort(sortByStatus());
        view.setOwner(ownerAccountId);

        return view;
    }

    private List<String> getAllHotelRfpBidViewColumnsForUserType(EntityType accountType) {
        switch (accountType){
            case HOTEL:
                return Arrays.asList("hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country", "bid.status", "bid.issuer.companyName", "bid.issuer.contactName", "hotelRfpBid.td.address", "hotelRfpBid.hotel.distance", "bid.currency", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.dyn", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.ia", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.scoreStanding");
            case CHAIN:
            case HMC:
                return Arrays.asList("hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country", "bid.status", "bid.issuer.companyName", "bid.issuer.contactName", "hotelRfpBid.td.address", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.dyn", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.ia", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.scoreStanding");
            case COMPANY:
            case TRAVEL_AGENCY:
            case TRAVEL_CONSULTANCY:
            default:
                return Arrays.asList("hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country", "bid.status", "bid.rfpName", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.chainName", "hotelRfpBid.hotel.brandName", "hotelRfpBid.hotel.rating", "hotelRfpBid.td.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.contactName", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.dyn", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.ia", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score");
        }
    }

    private Map<String, Object> createFilterForRfpType(RfpType rfpType) {
        final Map<String, Object> filter = new HashMap<>();
        filter.put("bid.rfpType", rfpType);
        filter.put("bid.status", Collections.singletonMap("$ne", "DELETED"));
        return filter;
    }


    private Map<String, Object> createFilterForHotelRfp(ObjectId rfpId) {
        final Map<String, Object> filter = new HashMap<>();
        filter.put("bid.rfp", rfpId.toString());
        filter.put("bid.status", Collections.singletonMap("$ne", "DELETED"));
        return filter;
    }
}
