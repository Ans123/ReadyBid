package net.readybid.auth.account.core;

import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.auth.permission.Permission;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.Location;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public interface Account {

    ObjectId getEntityId();

    CreationDetails getCreated();

    AccountStatusDetails getStatus();

    Long getLastChangeTimestamp();

    AccountStatus getStatusValue();

    void markLastChange();

    boolean hasPermission(ObjectId accountId, Permission permission);

    List<ObjectId> getAccountIdsWithPermission(Permission permission);

    ObjectId getId();

    EntityIndustry getIndustry();

    Address getAddress();

    String getWebsite();

    String getPhone();

    String getLogo();

    EntityType getType();

    String getName();

    String getEmailAddress();

    Location getLocation();

    ObjectId getPrimaryRepresentativeUserAccountId();

    Coordinates getCoordinates();
}
