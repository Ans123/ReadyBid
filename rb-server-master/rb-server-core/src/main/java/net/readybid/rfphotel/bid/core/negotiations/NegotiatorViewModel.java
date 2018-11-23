package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class NegotiatorViewModel implements ViewModel<Negotiator> {

    public static final ViewModelFactory<Negotiator, NegotiatorViewModel> FACTORY
            = NegotiatorViewModel::new;

    public ObjectId userId;
    public ObjectId userAccountId;
    public ObjectId accountId;
    public EntityType accountType;
    public BidManagerViewSide type;
    public String logo;
    public String profilePicture;
    public String companyName;
    public String firstName;
    public String lastName;
    public String jobTitle;



    public NegotiatorViewModel(Negotiator negotiator) {
        userId = negotiator.getUserId();
        userAccountId = negotiator.getUserAccountId();
        accountId = negotiator.getAccountId();
        accountType = negotiator.getAccountType();
        type = negotiator.getType();
        logo = negotiator.getAccountLogo();
        companyName = negotiator.getCompanyName();
        firstName = negotiator.getFirstName();
        lastName = negotiator.getLastName();
        jobTitle = negotiator.getJobTitle();
        profilePicture = negotiator.getProfilePicture();
    }
}
