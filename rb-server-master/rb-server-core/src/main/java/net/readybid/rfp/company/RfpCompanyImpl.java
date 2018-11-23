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
public class RfpCompanyImpl implements RfpCompany {

    private ObjectId entityId;
    private ObjectId accountId;

    private String name;
    private EntityType type;
    private EntityIndustry industry;
    private String website;
    private String logo;
    private String emailAddress;
    private String phone;
    private Location location;

    public void setEntityId(ObjectId entityId) {
        this.entityId = entityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public ObjectId getEntityId() {
        return entityId;
    }

    @Override
    public ObjectId getAccountId() {
        return accountId;
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
    public String getName() {
        return name;
    }

    @Override
    public Address getAddress() {
        return location == null ? null : location.getAddress();
    }

    @Override
    public String getFullAddress() {
        return location == null ? null : location.getFullAddress();
    }

    @Override
    public Coordinates getCoordinates() {
        return location == null ? null : location.getCoordinates();
    }

    @Override
    public String getWebsite() {
        return website;
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
    public Location getLocation() {
        return location;
    }

    public void setIndustry(EntityIndustry industry) {
        this.industry = industry;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
