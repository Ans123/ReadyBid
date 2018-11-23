package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class HotelRfpBidNegotiationsViewModel implements ViewModel<HotelRfpNegotiations> {

    public static final ViewModelFactory<HotelRfpNegotiations, HotelRfpBidNegotiationsViewModel> FACTORY
            = HotelRfpBidNegotiationsViewModel::new;

    public ObjectId bidId;
    public NegotiationsConfig config;
    public NegotiationsPartiesViewModel parties;
    public List<NegotiationViewModel> communication;

    public HotelRfpBidNegotiationsViewModel(HotelRfpNegotiations negotiations) {
        bidId = negotiations.getBidId();
        config = negotiations.getConfig();
        parties = NegotiationsPartiesViewModel.FACTORY.createAsPartial(negotiations.getParties());
        communication = NegotiationViewModel.FACTORY.createList(negotiations.getCommunication());
    }
}
