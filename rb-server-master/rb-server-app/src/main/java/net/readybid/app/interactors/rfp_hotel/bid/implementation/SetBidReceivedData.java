package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import org.bson.types.ObjectId;

public class SetBidReceivedData {
    public final Id bidId;
    public final HotelRfpBidState receivedState;
    public final QuestionnaireResponse response;

    SetBidReceivedData(ObjectId bidId, HotelRfpBidState receivedState, QuestionnaireResponse response) {
        this.bidId = Id.valueOf(bidId);
        this.receivedState = receivedState;
        this.response = response;
    }
}
