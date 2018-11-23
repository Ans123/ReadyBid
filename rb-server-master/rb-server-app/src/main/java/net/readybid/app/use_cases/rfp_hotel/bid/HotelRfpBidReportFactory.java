package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.app.use_cases.rfp_hotel.bid.implementation.reports.HotelRfpFinalAgreementReport;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;

import java.util.List;

public interface HotelRfpBidReportFactory {

    RbViewModel getGbtaAnswers(List<String> bids);

    RbViewModel getBidManagerFields(List<String> bidsIds, List<String> fields, AuthenticatedUser currentUser);

    RbListViewModel<HotelRfpFinalAgreementReport> getHotelRfpFinalAgreementReports(List<String> bidsIds, boolean includeUnsent);
}
