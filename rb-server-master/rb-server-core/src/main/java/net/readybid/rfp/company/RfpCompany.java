package net.readybid.rfp.company;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.Location;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface RfpCompany {

    ObjectId getEntityId();

    ObjectId getAccountId();

    EntityType getType();

    EntityIndustry getIndustry();

    String getName();

    Address getAddress();

    String getFullAddress();

    String getWebsite();

    String getEmailAddress();

    String getPhone();

    String getLogo();

    Location getLocation();

    Coordinates getCoordinates();
}
