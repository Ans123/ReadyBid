package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.core.ActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;

import java.util.List;

public interface HotelRfpBidActionReportProducer {

    List<ActionReport<HotelRfpBidQueryView>> create(List<Id> bidsIds, AuthenticatedUser currentUser, ActionReportBuilder<HotelRfpBidQueryView> actionReportBuilder);

    List<ActionReport<HotelRfpBidQueryView>> create(Id bidId, AuthenticatedUser currentUser);
}
