package net.readybid.app.gate.api.rfps.hotel;

import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.user.BasicUserDetailsViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.Date;
import java.util.Map;

public class HotelRfpBidStatusDetailsView implements ViewModel<HotelRfpBidStatusDetails> {

    public static final ViewModelFactory<HotelRfpBidStatusDetails, HotelRfpBidStatusDetailsView> FACTORY = HotelRfpBidStatusDetailsView::new;

    public String id;
    public String label;
    public Date at;
    public BasicUserDetailsViewModel by;
    public Map<String, Object> properties;

    public HotelRfpBidStatusDetailsView(HotelRfpBidStatusDetails details) {
        id = details.status.id;
        label = details.label;
        at = details.at;
        by = BasicUserDetailsViewModel.FACTORY.createAsPartial(details.by);
        properties = details.properties;
    }
}
