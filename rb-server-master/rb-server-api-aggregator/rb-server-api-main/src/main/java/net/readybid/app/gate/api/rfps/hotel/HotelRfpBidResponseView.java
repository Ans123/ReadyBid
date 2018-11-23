package net.readybid.app.gate.api.rfps.hotel;

import net.readybid.app.gate.api.rfps.QuestionnaireView;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 4/13/2017.
 *
 */
public class HotelRfpBidResponseView implements ViewModel<HotelRfpBid> {

    public static final ViewModelFactory<HotelRfpBid, HotelRfpBidResponseView> FACTORY = HotelRfpBidResponseView::new;

//    public ObjectId id;
//    public RfpSpecificationsViewModel specifications;
    public QuestionnaireView questionnaire;
//    public QuestionnaireResponseView response;
//    public HotelRfpSupplierViewModel supplier;
//    public BuyerViewModel buyer;

    public HotelRfpBidResponseView(HotelRfpBid bid) {
//        id = bid.getId();
//        specifications = RfpSpecificationsViewModel.FACTORY.createAsPartial(bid.getSpecifications());
        questionnaire = QuestionnaireView.FACTORY.createAsPartial(bid.getQuestionnaire());
//        response = QuestionnaireResponseView.FACTORY.createAsPartial(bid.getResponse());
//        supplier = HotelRfpSupplierViewModel.FACTORY.createAsPartial(bid.getSupplier());
//        buyer = BuyerViewModel.FACTORY.createAsPartial(bid.getBuyer());
    }
}
