package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.app.core.entities.entity.EntityType;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 7/25/2017.
 */
public interface Negotiator {
    ObjectId getUserId();

    ObjectId getUserAccountId();

    ObjectId getAccountId();

    EntityType getAccountType();

    String getAccountLogo();

    String getCompanyName();

    String getFirstName();

    String getLastName();

    String getJobTitle();

    String getProfilePicture();

    BidManagerViewSide getType();
}
