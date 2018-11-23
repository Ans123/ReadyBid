package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DejanK on 7/25/2017.
 */
public class NegotiationViewModel implements ViewModel<Negotiation> {

    public static final ViewModelFactory<Negotiation, NegotiationViewModel> FACTORY
            = NegotiationViewModel::new;

    public ObjectId _id;
    public Negotiator from;
    public String message;
    public Date at;
    public NegotiationValues values;


    public NegotiationViewModel(Negotiation negotiation) {
        _id = negotiation.getId();
        from = negotiation.getFrom();
        message = negotiation.getMessage();
        at = negotiation.getAt();
        values = negotiation.getValues();
    }
}
