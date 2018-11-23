package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 7/25/2017.
 */
public class NegotiationsPartiesViewModel implements ViewModel<NegotiationsParties> {

    public static final ViewModelFactory<NegotiationsParties, NegotiationsPartiesViewModel> FACTORY
            = NegotiationsPartiesViewModel::new;

    public NegotiatorViewModel buyer;
    public NegotiatorViewModel supplier;

    public NegotiationsPartiesViewModel(NegotiationsParties negotiationsParties) {
        buyer = NegotiatorViewModel.FACTORY.createAsPartial(negotiationsParties.getBuyer());
        supplier = NegotiatorViewModel.FACTORY.createAsPartial(negotiationsParties.getSupplier());
    }
}
