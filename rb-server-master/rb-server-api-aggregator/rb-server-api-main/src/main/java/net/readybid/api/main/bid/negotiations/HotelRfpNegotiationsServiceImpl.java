package net.readybid.api.main.bid.negotiations;

import net.readybid.api.main.bid.hotelrfp.BidResponseService;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.negotiation.HotelRfpNegotiationFactory;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.entities.Id;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
@Service
public class HotelRfpNegotiationsServiceImpl implements HotelRfpNegotiationsService {

    private final HotelRfpBidNegotiationsRepository repository;
    private final HotelRfpNegotiationFactory negotiationFactory;
    private final BidResponseService bidResponseService; // todo: try to replace with OfferFactory
    private final HotelRfpBidActionReportProducer actionReportProducer;
    private final HotelRfpNegotiationsMailService hotelRfpNegotiationsMailService;
    private final BidRepository bidRepository;
    private final HotelRfpBidStateFactory bidStateFactory;

    @Autowired
    public HotelRfpNegotiationsServiceImpl(
            HotelRfpBidNegotiationsRepository repository,
            HotelRfpNegotiationFactory negotiationFactory,
            BidResponseService bidResponseService,
            HotelRfpBidActionReportProducer actionReportProducer, HotelRfpNegotiationsMailService hotelRfpNegotiationsMailService,
            BidRepository bidRepository,
            HotelRfpBidStateFactory bidStateFactory
    ) {
        this.repository = repository;
        this.negotiationFactory = negotiationFactory;
        this.bidResponseService = bidResponseService;
        this.actionReportProducer = actionReportProducer;
        this.hotelRfpNegotiationsMailService = hotelRfpNegotiationsMailService;
        this.bidRepository = bidRepository;
        this.bidStateFactory = bidStateFactory;
    }

    @Override
    public HotelRfpNegotiations getNegotiations(String bidId) {
        return repository.getNegotiations(bidId);
    }

    @Override
    public RbViewModel addNegotiation(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser currentUser)
    {
        /**
         * TODO:
         * AUDIT TRAIL
         */
        final HotelRfpNegotiations negotiations = repository.getNegotiations(bidId);
        final BidManagerViewSide side = BidManagerViewSide.determineSide(RfpType.HOTEL, currentUser.getAccountType());
        final HotelRfpNegotiation negotiation = negotiationFactory.create(negotiationRequest, negotiations.getLastCommunication(), currentUser);

        if(BidManagerViewSide.BUYER.equals(side)){
            final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_SENT, currentUser);
            repository.addNegotiation(bidId, negotiation, state);
            hotelRfpNegotiationsMailService.sendNewNegotiationEmail(negotiations, currentUser);
        } else if(BidManagerViewSide.SUPPLIER.equals(side)){
            final HotelRfpBidOffer offer = bidResponseService.createOffer(negotiations.getConfig(), negotiation);
            final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_RESPONDED, currentUser);
            repository.addNegotiation(bidId, negotiation, offer, state);
        }
        return new RbListViewModel<>(actionReportProducer.create(new Id(bidId), currentUser));
    }

    @Override
    public RbViewModel updateBidNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser currentUser)
    {
        /**
         * TODO: FAILURE DUE TO CHANGE
         * AUDIT TRAIL
         *
         */
        final HotelRfpNegotiations negotiations = repository.getNegotiations(bidId);
        final BidManagerViewSide side = BidManagerViewSide.determineSide(RfpType.HOTEL, currentUser.getAccountType());
        final HotelRfpNegotiation negotiation = negotiationFactory.create(negotiationId, negotiationRequest, negotiations.getSecondLastCommunication(), currentUser);

        if(BidManagerViewSide.BUYER.equals(side)){
            final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_SENT, currentUser);
            repository.updateNegotiation(bidId, negotiation, state);
            hotelRfpNegotiationsMailService.sendNewNegotiationEmail(negotiations, currentUser);
        } else if(BidManagerViewSide.SUPPLIER.equals(side)){
            final HotelRfpBidOffer offer = bidResponseService.createOffer(negotiations.getConfig(), negotiation);
            final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_RESPONDED, currentUser);
            repository.updateNegotiation(bidId, negotiation, offer, state);
        }
        return new RbListViewModel<>(actionReportProducer.create(new Id(bidId), currentUser));
    }

    @Override
    public RbViewModel addAndFinalizeNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser currentUser)
    {
        /**
         * TODO:
         * AUDIT TRAIL
         * TODO: FAILURE DUE TO CHANGE
         */
        final HotelRfpNegotiations negotiations = repository.getNegotiations(bidId);
        final QuestionnaireResponse response = bidRepository.getResponse(bidId);
        final HotelRfpNegotiation negotiation = negotiationFactory.create(negotiationRequest, negotiations.getLastCommunication(), currentUser);
        final HotelRfpBidOffer offer = bidResponseService.createOffer(negotiations.getConfig(), negotiation);
        final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_FINALIZED, currentUser);

        bidResponseService.updateResponseWithOffer(response, negotiation.getValues());

        repository.addAndFinalize(bidId, response, negotiation, offer, state);
        hotelRfpNegotiationsMailService.sendNegotiationFinalizedEmail(negotiations, negotiation, currentUser);

        return new RbListViewModel<>(actionReportProducer.create(new Id(bidId), currentUser));
    }

    @Override
    public RbViewModel updateAndFinalizeNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser currentUser)
    {
        /**
         * TODO:
         * AUDIT TRAIL
         * TODO: FAILURE DUE TO CHANGE
         */

        final HotelRfpNegotiations negotiations = repository.getNegotiations(bidId);
        final QuestionnaireResponse response = bidRepository.getResponse(bidId);
        final HotelRfpNegotiation negotiation = negotiationFactory.create(negotiationId, negotiationRequest, negotiations.getSecondLastCommunication(), currentUser);
        final HotelRfpBidOffer offer = bidResponseService.createOffer(negotiations.getConfig(), negotiation);
        final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.NEGOTIATION_FINALIZED, currentUser);

        bidResponseService.updateResponseWithOffer(response, negotiation.getValues());
        repository.updateAndFinalize(bidId, response, negotiation, offer, state);
        hotelRfpNegotiationsMailService.sendNegotiationFinalizedEmail(negotiations, negotiation, currentUser);

        return new RbListViewModel<>(actionReportProducer.create(new Id(bidId), currentUser));
    }
}
