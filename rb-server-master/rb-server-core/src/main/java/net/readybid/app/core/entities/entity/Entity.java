package net.readybid.app.core.entities.entity;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.Location;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public interface Entity {

    ObjectId getId();

    EntityType getType();

    EntityIndustry getIndustry();

    void setForValidation(BasicUserDetails user);

    String getName();

    String getFullAddress();

    Location getLocation();

    String getWebsite();

    EntityImage getImage();

    Address getAddress();

    String getEmailAddress();

    String getPhone();

    String getLogo();

    String getImageUrl();

    String getStatusValue();

    boolean isActive();
}
