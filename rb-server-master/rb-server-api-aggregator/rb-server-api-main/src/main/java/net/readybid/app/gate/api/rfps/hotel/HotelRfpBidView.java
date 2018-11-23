package net.readybid.app.gate.api.rfps.hotel;

import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.gate.api.rfps.QuestionnaireView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.buyer.BuyerViewModel;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfp.specifications.RfpSpecificationsViewModel;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.rfphotel.destinations.web.TravelDestinationViewModel;
import net.readybid.rfphotel.supplier.HotelRfpSupplierViewModel;
import net.readybid.web.ViewModel;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 2/4/2017.
 *
 */
public class HotelRfpBidView implements ViewModel<HotelRfpBid> {


    public ObjectId id;
    public HotelRfpType type;
    public ObjectId rfpId;
    public RfpSpecificationsViewModel specifications;
    public HotelRfpSupplierViewModel supplier;
    public BuyerViewModel buyer;
    public String coverLetter;
    public QuestionnaireView questionnaire;
    public String finalAgreement;
    public TravelDestinationViewModel travelDestination;
    public double distanceKm;
    public double distanceMi;
    public HotelRfpBidStatusDetailsView state;

    public HotelRfpBidView(HotelRfpBid bid, AuthenticatedUser currentUser) {
        id = bid.getId();
        type = bid.getType();
        rfpId = bid.getRfpId();
        specifications = RfpSpecificationsViewModel.FACTORY.createAsPartial(bid.getSpecifications());
        supplier = HotelRfpSupplierViewModel.FACTORY.createAsPartial(bid.getSupplier());
        buyer = BuyerViewModel.FACTORY.createAsPartial(bid.getBuyer());
        coverLetter = bid.getCoverLetter();
        questionnaire = QuestionnaireView.FACTORY.createAsPartial(bid.getQuestionnaire());
        finalAgreement = bid.getFinalAgreementTemplate();
        travelDestination = TravelDestinationViewModel.FACTORY.createAsPartial(bid.getSubject());
        distanceKm = bid.getDistance(DistanceUnit.KM);
        distanceMi = bid.getDistance(DistanceUnit.MI);
        state = HotelRfpBidStatusDetailsView.FACTORY.createAsPartial(getStatusDetails(bid, currentUser));
    }

    public HotelRfpBidView(HotelRfpBid bid) {
        this(bid, null);
    }

    private HotelRfpBidStatusDetails getStatusDetails(HotelRfpBid bid, AuthenticatedUser currentUser) {
        return currentUser == null || RfpType.HOTEL.isSupplier(currentUser.getAccountType())
                ? bid.getSupplierStatusDetails()
                : bid.getBuyerStatusDetails();
    }
}
