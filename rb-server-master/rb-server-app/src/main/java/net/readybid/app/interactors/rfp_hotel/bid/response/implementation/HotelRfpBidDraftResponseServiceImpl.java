package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBid;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidDraftResponseService;
import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelRfpBidDraftResponseServiceImpl implements HotelRfpBidDraftResponseService {

    private final HotelRfpBidResponseStorageManager bidResponseStorageManager;
    private final HotelRfpBidResponseLoader bidLoader;
    private final HotelRfpBidResponseToHotelMatcher matcher;
    private final HotelRfpBidQuestionnaireResponseProducerImpl producer;
    private final HotelRfpBidStateFactory bidStateFactory;

    @Autowired
    public HotelRfpBidDraftResponseServiceImpl(
            HotelRfpBidResponseStorageManager bidResponseStorageManager,
            HotelRfpBidResponseLoader bidLoader,
            HotelRfpBidResponseToHotelMatcher matcher,
            HotelRfpBidQuestionnaireResponseProducerImpl producer,
            HotelRfpBidStateFactory bidStateFactory
    ) {
        this.bidResponseStorageManager = bidResponseStorageManager;
        this.bidLoader = bidLoader;
        this.matcher = matcher;
        this.producer = producer;
        this.bidStateFactory = bidStateFactory;
    }

    @Override
    public List<HotelRfpBid> saveDraftResponses(List<String> bidsIds, List<Map<String, String>> validatedResponses, Date responsesAt, BasicUserDetails currentUser) {
        final List<HotelRfpBid> bids = bidLoader.getBidsWithQuestionnaireHotelIdResponseContextFields(bidsIds);
        final List<HotelRfpBid> responses = prepareResponses(bids, validatedResponses, responsesAt, currentUser);
        if(!(responses == null || responses.isEmpty())) bidResponseStorageManager.saveDraftResponses(responses);
        return responses;
    }

    @Override
    public HotelRfpBid saveDraftResponse(String bidId, Map<String, String> answers, Date responseAt, AuthenticatedUser currentUser) {
        final HotelRfpBid response = prepareResponse(bidId, answers, responseAt, currentUser);
        if(response != null) bidResponseStorageManager.saveDraftResponses(Collections.singletonList(response));
        return response;
    }

    private HotelRfpBid prepareResponse(String bidId, Map<String, String> answers, Date responseAt, AuthenticatedUser currentUser) {
        final HotelRfpBid bid = bidLoader.getBidWithQuestionnaireHotelIdResponseContextFields(bidId);
        final QuestionnaireResponse qResponse = producer.prepareDraftResponse(answers, bid);
        if(qResponse != null) {
            qResponse.touch();
            return newBidWithResponseDraft(bid, qResponse, responseAt, currentUser);
        }
        return null;
    }

    private List<HotelRfpBid> prepareResponses(
            List<HotelRfpBid> bids,
            List<Map<String, String>> validatedResponses,
            Date responsesAt,
            BasicUserDetails currentUser
    ) {
        final Map<HotelRfpBid, Map<String, String>> matchedResponses = matcher.matchBidsToResponses(bids, validatedResponses);

        final List<HotelRfpBid> bidsWithResponses = new ArrayList<>();
        for(Map.Entry<HotelRfpBid, Map<String, String>> matchedResponse : matchedResponses.entrySet()){
            final HotelRfpBid bid = matchedResponse.getKey();
            final Map<String, String> bidResponse = matchedResponse.getValue();
            final QuestionnaireResponse qResponse = producer.prepareDraftResponse(bidResponse, bid);
            if(qResponse != null) {
                qResponse.touch();
                bidsWithResponses.add(newBidWithResponseDraft(bid, qResponse, responsesAt, currentUser));
            }
        }
        return bidsWithResponses;
    }

    private HotelRfpBid newBidWithResponseDraft(HotelRfpBid oldBid, QuestionnaireResponse response, Date at, BasicUserDetails currentUser) {
        final HotelRfpBidState receivedState = bidStateFactory.createReceivedState(oldBid.getBuyerStatusDetails(), response.getErrorsCount(), response.isTouched(), at, currentUser);

        return new AbstractHotelRfpBid(){
            @Override
            public ObjectId getId(){
                return oldBid.getId();
            }

            @Override
            public HotelRfpBidState getState(){
                return receivedState;
            }

            @Override
            public QuestionnaireResponse getResponseDraft(){
                return response;
            }
        };
    }
}
