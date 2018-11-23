package net.readybid.entities.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityImage;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public class EntityImpl implements Entity {

    private ObjectId id;

    private EntityType type;

    private String name;

    private EntityIndustry industry;

    private String website;

    private String emailAddress;

    private String phone;

    private String logo;

    private EntityImage image;

    @JsonDeserialize(as = LocationImpl.class)
    private Location location;

    private CreationDetails created;

    private StatusDetails<EntityStatus> status;

    public EntityImpl() {}

    protected EntityImpl(LocationImpl location) {
        setLocation(location);
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public EntityIndustry getIndustry() {
        return industry;
    }

    @Override
    public void setForValidation(BasicUserDetails user) {
        created = new CreationDetails(user);
        status = new EntityStatusDetails(created, EntityStatus.PENDING);
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return location == null ? null : location.getAddress();
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.getUrl();
    }

    @Override
    public String getStatusValue() {
        final EntityStatus entityStatus = status == null ? null : status.getValue();
        return entityStatus == null ? null : String.valueOf(entityStatus);
    }

    @Override
    public boolean isActive() {
        final EntityStatus entityStatus = status == null ? null : status.getValue();
        return entityStatus == null ? false : entityStatus.isActive();
    }

    public String getFullAddress() {
        return location == null ? null : location.getFullAddress();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public EntityImage getImage() {
        return image;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setIndustry(EntityIndustry industry) {
        this.industry = industry;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setImage(EntityImage image) {
        this.image = image;
    }

    public void setStatus(StatusDetails<EntityStatus> status) {
        this.status = status;
    }

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    public CreationDetails getCreated() {
        return created;
    }

    public StatusDetails<EntityStatus> getStatus() {
        return status;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
