package net.readybid.user;

import net.readybid.email.WithInternetAddress;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface BasicUserDetails extends WithInternetAddress  {

    ObjectId getId();

    String getFirstName();

    String getLastName();

    String getFullName();

    String getEmailAddress();

    String getPhone();
}
