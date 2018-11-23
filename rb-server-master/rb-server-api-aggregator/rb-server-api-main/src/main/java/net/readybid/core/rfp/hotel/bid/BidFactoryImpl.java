package net.readybid.core.rfp.hotel.bid;

import net.readybid.api.main.bid.supplier.HotelRfpSupplierFactory;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.rfp.QuestionnaireResponseImpl;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.entities.rfp.questionnaire.QuestionnaireImpl;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
@Service
public class BidFactoryImpl implements BidFactory {

    private final HotelRfpSupplierFactory hotelRfpSupplierFactory;
    private final HotelRfpBidStateFactory stateFactory;

    @Autowired
    public BidFactoryImpl(
            HotelRfpSupplierFactory hotelRfpSupplierFactory,
            HotelRfpBidStateFactory stateFactory
    ) {
        this.hotelRfpSupplierFactory = hotelRfpSupplierFactory;
        this.stateFactory = stateFactory;
    }

    @Override
    public HotelRfpBid create(Rfp rfp, TravelDestination destination, HotelRfpBidBuilder hotelRfpBidBuilder, AuthenticatedUser currentUser) {
        final HotelRfpBidImpl bid = createBid(new ObjectId(), rfp, destination, hotelRfpBidBuilder);
        final CreationDetails creationDetails = new CreationDetails(currentUser);
        bid.setCreationDetails(creationDetails);
        bid.setState(stateFactory.createSimpleState(HotelRfpBidStateStatus.CREATED, creationDetails.getBy(), creationDetails.getAt()));
        return bid;
    }

    @Override
    public HotelRfpBid recreate(HotelRfpBid oldBid, Rfp rfp, TravelDestination destination, HotelRfpBidBuilder hotelRfpBidBuilder, AuthenticatedUser currentUser) {
        final HotelRfpBidImpl bid = createBid(oldBid.getId(), rfp, destination, hotelRfpBidBuilder);

        bid.setCreationDetails(oldBid.getCreationDetails());
        bid.setState(stateFactory.createSimpleState(HotelRfpBidStateStatus.CREATED, currentUser));
        return bid;
    }

    private HotelRfpBidImpl createBid(ObjectId bidId, Rfp rfp, TravelDestination destination, HotelRfpBidBuilder hotelRfpBidBuilder) {
        final HotelRfpBidImpl bid = new HotelRfpBidImpl();

        bid.setId(bidId);
        bid.setType(HotelRfpType.DIRECT);
        bid.setRfp(rfp);
        bid.setSubject(destination);
        bid.setBuyer(rfp.getBuyer());
        bid.setSupplier(hotelRfpSupplierFactory.create(hotelRfpBidBuilder));
        bid.setQuestionnaire(createQuestionnaire(rfp.getQuestionnaire(), hotelRfpBidBuilder.getDefaultResponse()));

        bid.updateDistance();
        bid.updateAddressAnalytics();

        return bid;
    }

    private Questionnaire createQuestionnaire(Questionnaire rfpQuestionnaire, HotelRfpDefaultResponse defaultResponse) {
        final QuestionnaireImpl q = new QuestionnaireImpl();
        q.setType(rfpQuestionnaire.getType());
        q.setModel(rfpQuestionnaire.getModel());
        q.setConfig(rfpQuestionnaire.getConfig());
        q.setResponse(createResponse(rfpQuestionnaire.getResponse(), defaultResponse));
        return q;
    }

    private QuestionnaireResponse createResponse(
            QuestionnaireResponse rfpResponse,
            HotelRfpDefaultResponse defaultHotelResponse
    ) {
        final QuestionnaireResponseImpl response = new QuestionnaireResponseImpl();
        response.setState(rfpResponse.getState());
        response.setIsValid(false);
        final Map<String,String> answers = new HashMap<>(rfpResponse.getAnswers());
        if(defaultHotelResponse.answers != null) answers.putAll(defaultHotelResponse.answers);
        response.setAnswers(answers);

        return response;
    }
}
