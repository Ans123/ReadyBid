package net.readybid.app.interactors.rfp_hotel.bid.response.implementation;

import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidResponseService;
import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.app.interactors.rfp_hotel.default_response.HotelRfpDefaultResponseService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HotelRfpBidResponseServiceImpl implements HotelRfpBidResponseService {

    private final HotelRfpBidResponseStorageManager bidResponseStorageManager;
    private final HotelRfpBidResponseLoader bidLoader;
    private final HotelRfpDefaultResponseService defaultAnswersService;
    private final HotelRfpBidResponseProducer responseProducer;

    @Autowired
    public HotelRfpBidResponseServiceImpl(
            HotelRfpBidResponseStorageManager bidResponseStorageManager,
            HotelRfpBidResponseLoader bidLoader,
            HotelRfpDefaultResponseService defaultAnswersService,
            HotelRfpBidResponseProducer responseProducer
    ) {
        this.bidResponseStorageManager = bidResponseStorageManager;
        this.bidLoader = bidLoader;
        this.defaultAnswersService = defaultAnswersService;
        this.responseProducer = responseProducer;
    }

    @Override
    public List<HotelRfpBid> setFromDrafts(List<String> bidsIds, boolean ignoreErrors, Date responsesAt, AuthenticatedUser currentUser) {
        final List<HotelRfpBid> bids = bidLoader.loadBidsWithDraftToResponseContext(bidsIds);
        final List<HotelRfpBid> updatedBids = responseProducer.setResponses(bids, ignoreErrors, responsesAt, currentUser);
        saveResponses(updatedBids);
        return updatedBids;
    }

    @Override
    public HotelRfpBid setResponse(String bidId, Map<String, String> response, boolean ignoreErrors, AuthenticatedUser currentUser, Date responseAt) {
        final HotelRfpBid bid = bidLoader.loadBidWithSetResponseContext(bidId);
        final HotelRfpBid updatedBid = responseProducer.setResponse(bid, response, ignoreErrors, currentUser, responseAt);
        saveResponses(Collections.singletonList(updatedBid));
        return updatedBid;
    }

    private void saveResponses(List<HotelRfpBid> updatedBids) {
        bidResponseStorageManager.saveResponses(updatedBids);
        defaultAnswersService.update(updatedBids);
    }
}
