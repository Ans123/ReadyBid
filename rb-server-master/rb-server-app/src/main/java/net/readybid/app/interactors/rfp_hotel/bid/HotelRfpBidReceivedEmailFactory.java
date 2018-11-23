package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.user.BasicUserDetails;

import java.util.List;

public interface HotelRfpBidReceivedEmailFactory {

    List<EmailTemplate> createAll(List<ActionReport<HotelRfpBidQueryView>> actionReports, BasicUserDetails currentUser);
}
