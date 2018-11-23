package net.readybid.rfp.bidmanagerview;

import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewImpl;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.bidmanagerview.BidManagerViewType;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.type.RfpType;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
@Service
public class BidManagerViewFactoryImpl implements BidManagerViewFactory {

    @Override
    public BidManagerView createRfpViewForGuest(String viewName, Rfp rfp) {
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(rfp.getId());
        view.setRfpType(RfpType.HOTEL);
        view.setType(BidManagerViewType.RFP);
        view.setRfpId(rfp.getId());
        view.setSide(BidManagerViewSide.BUYER);
        view.setName(viewName);
        view.setColumns(Arrays.asList("bid.status", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.rating", "hotelRfpBid.td.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.contactName", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score"));
//        view.setColumns(Arrays.asList("hotelRfpBid.hotel.info", "hotelRfpBid.hotel.contact", "hotelRfpBid.td.info", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.rating", "hotelRfpBid.sslyrDiff", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score", "bid.status"));

        view.setFilter(createFilterForHotelRfp(rfp.getId(), rfp.getCreationDetails()));
        view.setSort(sortByStatus());
        view.setGroup("hotelRfpBid.td.name");
//        view.setGroup("hotelRfpBid.td.info");
        view.setOwner(rfp.getCreationDetails().getBy().getId());

        return view;
    }

    private Map<String, Object> sortByStatus() {
        final Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("bid.status", -1);
        return sortMap;
    }

    @Override
    public String createNameForRfpView(String rfpName) {
        return String.format("%s View", rfpName);
    }

    @Override
    public List<BidManagerView> createMockedOtherRfpTypeViews(ObjectId guestId) {
        final List<BidManagerView> defaultViews = new ArrayList<>();

        defaultViews.add(createGenericDemoView("Expense Management", RfpType.EXPENSE_MANAGEMENT, guestId));
        defaultViews.add(createGenericDemoView("Virtual Card", RfpType.VIRTUAL_CARD, guestId));
        defaultViews.add(createGenericDemoView("Limousine", RfpType.LIMOUSINE, guestId));
        defaultViews.add(createGenericDemoView("Crew Accommodation", RfpType.CREW_ACCOMMODATION, guestId));
        defaultViews.add(createGenericDemoView("Corporate Card", RfpType.CORPORATE_CARD, guestId));
        defaultViews.add(createGenericDemoView("Air", RfpType.AIRLINE, guestId));
        defaultViews.add(createGenericDemoView("Meetings", RfpType.MEETINGS, guestId));
        defaultViews.add(createGenericDemoView("Group", RfpType.GROUP, guestId));
        defaultViews.add(createGenericDemoView("TMC", RfpType.TMC, guestId));

        return defaultViews;
    }

    @Override
    public BidManagerView createDefaultViewForGuest(ObjectId id) {
        return createAllHotelRfpBidsView(id);
    }

    private BidManagerView createGenericDemoView(String typeName, RfpType type, ObjectId guestId) {
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(new ObjectId());
        view.setType(BidManagerViewType.ALL);
        view.setName(String.format("All %s RFP Bids", typeName));
        view.setColumns(Arrays.asList("bid.rfp", "hotelRfpBid.tcos", "hotelRfpBid.score", "bid.status"));
        view.setFilter(createFilterForRfpType(type, guestId));
        view.setOwner(guestId);

        return view;
    }

    private BidManagerView createAllHotelRfpBidsView(ObjectId guestId) {
        final BidManagerViewImpl view = new BidManagerViewImpl();

        view.setId(new ObjectId());
        view.setRfpType(RfpType.HOTEL);
        view.setType(BidManagerViewType.ALL);
        view.setName("All Hotel RFP Bids");

        view.setSide(BidManagerViewSide.BUYER);
        view.setColumns(Arrays.asList("bid.status", "bid.rfpName", "hotelRfpBid.hotel.name", "hotelRfpBid.hotel.rating", "hotelRfpBid.td.name", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.contactName", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.sslyrDiff", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score"));
//        view.setColumns(Arrays.asList("bid.rfp", "hotelRfpBid.hotel.info", "hotelRfpBid.hotel.contact", "hotelRfpBid.td.info", "hotelRfpBid.hotel.distance", "hotelRfpBid.hotel.rating", "hotelRfpBid.sslyrDiff", "hotelRfpBid.lra", "hotelRfpBid.nlra", "hotelRfpBid.localTax", "hotelRfpBid.wifi", "hotelRfpBid.breakfast", "hotelRfpBid.parking", "hotelRfpBid.fitness", "hotelRfpBid.earlyCheckout", "hotelRfpBid.airportShuttle", "hotelRfpBid.tcos", "hotelRfpBid.score", "bid.status"));

        view.setFilter(createFilterForRfpType(RfpType.HOTEL, guestId));
        view.setSort(sortByStatus());
        view.setGroup("bid.rfpName");
        view.setOwner(guestId);

        return view;
    }

    private Map<String, Object> createFilterForRfpType(RfpType rfpType, ObjectId guestId) {
        final Map<String, Object> filter = new HashMap<>();
        filter.put("bid.rfpType", rfpType);
        filter.put("bid.createdBy", guestId);
        filter.put("bid.status", Collections.singletonMap("$ne", "DELETED"));
        return filter;
    }


    private Map<String, Object> createFilterForHotelRfp(ObjectId rfpId, CreationDetails rfpCreationDetails) {
        final Map<String, Object> filter = new HashMap<>();
        filter.put("bid.rfp", rfpId.toString());
        filter.put("bid.createdBy", rfpCreationDetails.getBy().getId());
        filter.put("bid.status", Collections.singletonMap("$ne", "DELETED"));
        return filter;
    }
}
