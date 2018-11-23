package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBid;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.negotiation.HotelRfpNegotiationsFactory;
import net.readybid.app.interactors.rfp_hotel.bid.offer.CreateHotelRfpBidOfferAction;
import net.readybid.auth.rfp.contact.RfpContactFactory;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.exceptions.BadRequestException;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class HotelRfpBidResponseProducer {

    private final HotelRfpBidStateFactory bidStateFactory;
    private final HotelRfpNegotiationsFactory negotiationsFactory;
    private final RfpContactFactory rfpContactFactory;
    private final CreateHotelRfpBidOfferAction createHotelRfpBidOfferAction;
    private final HotelRfpBidQuestionnaireResponseProducerImpl responseProducer;

    HotelRfpBidResponseProducer(
            HotelRfpBidStateFactory bidStateFactory,
            HotelRfpNegotiationsFactory negotiationsFactory,
            RfpContactFactory rfpContactFactory,
            CreateHotelRfpBidOfferAction createHotelRfpBidOfferAction,
            HotelRfpBidQuestionnaireResponseProducerImpl responseProducer
    ) {
        this.bidStateFactory = bidStateFactory;
        this.negotiationsFactory = negotiationsFactory;
        this.rfpContactFactory = rfpContactFactory;
        this.createHotelRfpBidOfferAction = createHotelRfpBidOfferAction;
        this.responseProducer = responseProducer;
    }

    List<HotelRfpBid> setResponses(List<HotelRfpBid> bids, boolean ignoreErrors, Date responsesAt, AuthenticatedUser currentUser) {
        if(!ignoreErrors) areAllResponsesAcceptable(bids);
        return setResponses(bids, responsesAt, currentUser);
    }

    HotelRfpBid setResponse(HotelRfpBid bid, Map<String, String> response, boolean ignoreErrors, AuthenticatedUser currentUser, Date responseAt) {
        final QuestionnaireResponse qResponse = responseProducer.prepareResponse(response, bid);
        if(qResponse == null) return null;
        if(!ignoreErrors) isResponseAcceptable(qResponse, Id.asString(bid.getId()));
        return new PartialHotelRfpBid(bid, currentUser, qResponse, responseAt);
    }

    private static void areAllResponsesAcceptable(List<HotelRfpBid> bids) {
        bids.forEach(b -> isResponseAcceptable(b.getResponseDraft(), String.valueOf(b.getId())));
    }

    private static void isResponseAcceptable(QuestionnaireResponse qResponse, String bidId) {
        if(qResponse == null || !qResponse.isValid())
            throw new BadRequestException("RESPONSE_NOT_VALID", "Invalid Response", bidId);
    }

    private List<HotelRfpBid> setResponses(List<HotelRfpBid> bids, Date responsesAt, AuthenticatedUser currentUser) {
        return bids.stream()
                .map(b -> new PartialHotelRfpBid(b, currentUser, b.getResponseDraft(), responsesAt))
                .collect(Collectors.toList());
    }

    private class PartialHotelRfpBid extends AbstractHotelRfpBid {

        private final ObjectId bidId;
        private final ObjectId hotelId;
        private final QuestionnaireResponse response;
        private final HotelRfpNegotiations negotiations;
        private final HotelRfpBidOffer offer;
        private final HashMap<String, Object> analytics;
        private final RfpContact rfpContact;
        private final HotelRfpBidState state;

        private PartialHotelRfpBid(HotelRfpBid bid, AuthenticatedUser currentUser, QuestionnaireResponse response, Date responsesAt) {
            final Map<String, String> answers = response.getAnswers();
            this.bidId = bid.getId();
            this.hotelId = bid.getSupplierCompanyEntityId();
            this.response = response;
            this.negotiations = negotiationsFactory.createNegotiations(answers, currentUser);
            this.offer = createHotelRfpBidOfferAction
                    .createFromNegotiation(negotiations.getConfig(), negotiations.getLastCommunication().getValues());

            this.analytics = new HashMap<>(bid.getAnalytics());
            this.analytics.put("currency", answers.getOrDefault("RATE_CURR", "USD"));

            this.rfpContact = rfpContactFactory.createContact(currentUser.getCurrentUserAccount());
            this.state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.RESPONDED, currentUser, responsesAt);
        }

        @Override
        public ObjectId getId() { return bidId; }

        @Override
        public RfpContact getSupplierContact(){ return rfpContact; }

        @Override
        public ObjectId getSupplierCompanyEntityId(){ return hotelId; }

        @Override
        public HotelRfpBidOffer getOffer() { return offer; }

        @Override
        public Map<String, Object> getAnalytics() { return analytics; }

        @Override
        public HotelRfpNegotiations getNegotiations() { return negotiations; }

        @Override
        public QuestionnaireResponse getResponse() { return response; }

        @Override
        public HotelRfpBidState getState() { return state; }
    }
}
