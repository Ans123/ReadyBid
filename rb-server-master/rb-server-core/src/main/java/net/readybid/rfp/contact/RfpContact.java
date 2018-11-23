package net.readybid.rfp.contact;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.rfp.company.RfpCompany;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface RfpContact extends BasicUserDetails {
    RfpCompany getCompany();

    String getFirstName();

    String getLastName();

    String getFullName();

    String getEmailAddress();

    String getPhone();

    ObjectId getId();

    String getJobTitle();

    String getCompanyName();

    boolean isUser();

    ObjectId getCompanyAccountId();

    EntityType getCompanyType();
}
