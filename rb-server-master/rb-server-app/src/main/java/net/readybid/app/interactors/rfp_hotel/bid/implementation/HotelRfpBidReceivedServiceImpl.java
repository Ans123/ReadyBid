package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.interactors.authentication.user.gate.CurrentUserProvider;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidReceivedService;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidResponseStorageManager;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidQuestionnaireResponseProducer;
import net.readybid.app.interactors.rfp_hotel.bid.response.gate.HotelRfpBidResponseLoader;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HotelRfpBidReceivedServiceImpl implements HotelRfpBidReceivedService {

    private final CurrentUserProvider currentUserProvider;
    private final HotelRfpBidResponseLoader bidLoader;
    private final HotelRfpBidResponseStorageManager bidResponseStorageManager;
    private final HotelRfpBidStateFactory bidStateFactory;
    private final HotelRfpBidQuestionnaireResponseProducer bidResponseProducer;

    @Autowired
    public HotelRfpBidReceivedServiceImpl(
            CurrentUserProvider currentUserProvider,
            HotelRfpBidResponseLoader bidLoader,
            HotelRfpBidResponseStorageManager bidResponseStorageManager,
            HotelRfpBidStateFactory bidStateFactory,
            HotelRfpBidQuestionnaireResponseProducer bidResponseProducer
    ) {
        this.currentUserProvider = currentUserProvider;
        this.bidLoader = bidLoader;
        this.bidResponseStorageManager = bidResponseStorageManager;
        this.bidStateFactory = bidStateFactory;
        this.bidResponseProducer = bidResponseProducer;
    }

    @Override
    public void markBidAsReceivedByGuest(HotelRfpBid bid) {
        final RfpContact supplierContact = bid.getSupplierContact();
        if(Objects.equals(EntityType.CHAIN, supplierContact.getCompanyType()))
            markBidsAsReceived(bid.getRfpId(), supplierContact);
        else
            markBidAsReceived(String.valueOf(bid.getId()), supplierContact);
    }

    @Override
    public void markBidAsReceivedByUser(String bidId) {
        final AuthenticatedUser currentUser = currentUserProvider.get();
        if(currentUser != null && RfpType.HOTEL.isSupplier(currentUser.getAccountType())){
            markBidAsReceived(bidId, currentUser);
        }
    }

    private void markBidAsReceived(String bidId, BasicUserDetails currentUser) {
        final List<HotelRfpBid> bids = bidLoader.getBidsWithQuestionnaireHotelIdResponseContextFields(Collections.singletonList(bidId));
        markBidsAsReceived(bids, currentUser);
    }

    private void markBidsAsReceived(ObjectId rfpId, BasicUserDetails currentUser) {
        final List<HotelRfpBid> bids = bidLoader.getBidsWithQuestionnaireHotelIdResponseContextFieldsByRfpAndSupplierContact(rfpId, currentUser.getEmailAddress());
        markBidsAsReceived(bids, currentUser);
    }

    private void markBidsAsReceived(List<HotelRfpBid> bids, BasicUserDetails currentUser) {
        if(!bids.isEmpty()){
            final List<SetBidReceivedData> data = bids.stream()
                    .map(bid -> generateData(bid, currentUser))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            bidResponseStorageManager.setBidsAsReceived(data);
        }
    }

    private SetBidReceivedData generateData(HotelRfpBid bid, BasicUserDetails currentUser) {
        if(Objects.equals(HotelRfpBidStateStatus.SENT, bid.getStateStatus())) {
            final QuestionnaireResponse response = bidResponseProducer.prepareDraftResponse(bid);
            final HotelRfpBidState receivedState = bidStateFactory
                    .createReceivedState(bid.getBuyerStatusDetails(), response.getErrorsCount(), response.isTouched(), currentUser);
            return new SetBidReceivedData(bid.getId(), receivedState, response);
        }
        return null;
    }
}
