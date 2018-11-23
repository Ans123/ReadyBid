package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.core.ActionReportBuilder;
import net.readybid.entities.Id;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidQueryViewLoader;
import net.readybid.auth.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelRfpBidActionReportProducerImpl implements HotelRfpBidActionReportProducer {

    private final HotelRfpBidQueryViewLoader hotelRfpBidQueryViewLoader;

    @Autowired
    public HotelRfpBidActionReportProducerImpl(HotelRfpBidQueryViewLoader hotelRfpBidQueryViewLoader) {
        this.hotelRfpBidQueryViewLoader = hotelRfpBidQueryViewLoader;
    }

    @Override
    public List<ActionReport<HotelRfpBidQueryView>> create(
            List<Id> bidsIds,
            AuthenticatedUser currentUser,
            ActionReportBuilder<HotelRfpBidQueryView> reportBuilder
    ) {
        final Map<Id, HotelRfpBidQueryView> bids = loadBids(bidsIds, currentUser);
        return bidsIds.stream()
                .map(id -> reportBuilder.build(id, bids.get(id)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActionReport<HotelRfpBidQueryView>> create(Id bidId, AuthenticatedUser currentUser) {
        return create(Collections.singletonList(bidId), currentUser, new AbstractHotelRfpBidActionReportBuilder() {
            @Override
            protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
                return buildOkReport(bid);
            }
        });
    }

    private Map<Id, HotelRfpBidQueryView> loadBids(List<Id> bidsIds, AuthenticatedUser currentUser) {
        return hotelRfpBidQueryViewLoader.find(bidsIds, currentUser).stream()
                .collect(Collectors.toMap(v -> new Id(v.$bidId), v -> v));
    }
}
