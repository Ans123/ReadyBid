package net.readybid.app.use_cases.rfp_hotel.bid.implementation.reports;

import net.readybid.app.core.entities.rfp.QuestionnaireForm;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.interactors.rfp.gate.QuestionnaireTemplateLibrary;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidQueryViewLoader;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidLoader;
import net.readybid.app.use_cases.rfp_hotel.bid.HotelRfpBidReportFactory;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelRfpBidReportFactoryImpl implements HotelRfpBidReportFactory {

    private final HotelRfpBidLoader bidLoader;
    private final HotelRfpBidQueryViewLoader bidQueryViewLoader;
    private final QuestionnaireTemplateLibrary templateLibrary;

    @Autowired
    public HotelRfpBidReportFactoryImpl(HotelRfpBidLoader bidLoader, HotelRfpBidQueryViewLoader bidQueryViewLoader, QuestionnaireTemplateLibrary templateLibrary) {
        this.bidLoader = bidLoader;
        this.bidQueryViewLoader = bidQueryViewLoader;
        this.templateLibrary = templateLibrary;
    }

    @Override
    public RbViewModel getGbtaAnswers(List<String> bidsIds) {
        final List<HotelRfpBid> bids = bidLoader.getResponseAnswers(bidsIds);
        return new RbListViewModel<>(bids.stream().map(HotelRfpBid::getResponseAnswers).collect(Collectors.toList()));
    }

    @Override
    public RbViewModel getBidManagerFields(List<String> bidsIds, List<String> fields, AuthenticatedUser currentUser) {
        final List<HotelRfpBidQueryView> bids = bidQueryViewLoader.query(bidsIds, fields, currentUser);
        return new RbListViewModel<>(bids);
    }

    @Override
    public RbListViewModel<HotelRfpFinalAgreementReport> getHotelRfpFinalAgreementReports(List<String> bidsIds, boolean includeUnsent) {
        final List<HotelRfpBid> bids = bidLoader.getFinalAgreements(bidsIds, includeUnsent);
        final QuestionnaireForm template = templateLibrary.getTemplate();
        final List<HotelRfpFinalAgreementReport> finalAgreements = bids.stream()
                .map(b -> new HotelRfpFinalAgreementReport(template, b))
                .collect(Collectors.toList());
        return new RbListViewModel<>(finalAgreements);
    }
}
