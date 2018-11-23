package net.readybid.auth.account.core;

import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.permissions.Permissions;
import net.readybid.auth.permissions.PermissionsImpl;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.Location;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class AccountImpl implements Account {

    private ObjectId id;
    private ObjectId entityId;

    private String name;
    private EntityType type;
    private EntityIndustry industry;
    private Location location;

    private String website;
    private String logo;
    private String emailAddress;
    private String phone;

    private CreationDetails created;
    private AccountStatusDetails status;
    private Long changed;

    private Permissions permissions;
    private ObjectId primaryRepresentativeUserAccountId;

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setEntityId(ObjectId entityId) {
        this.entityId = entityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    public CreationDetails getCreated() {
        return created;
    }

    public void setStatus(AccountStatusDetails status) {
        this.status = status;
    }

    public ObjectId getEntityId() {
        return entityId;
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public EntityIndustry getIndustry() {
        return industry;
    }

    public String getName() {
        return name;
    }

    @Override
    public Address getAddress() {
        return location == null ? null : location.getAddress();
    }

    @Override
    public String getWebsite() {
        return website;
    }

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

    public AccountStatusDetails getStatus() {
        return status;
    }

    @Override
    public Long getLastChangeTimestamp() {
        return changed;
    }

    @Override
    public AccountStatus getStatusValue() {
        return status == null ? null : status.getValue();
    }

    @Override
    public void markLastChange() {
        this.changed = new Date().getTime();
    }

    @Override
    public boolean hasPermission(ObjectId accountId, Permission permission) {
        return hasPermission(String.valueOf(accountId), permission);
    }

    @Override
    public List<ObjectId> getAccountIdsWithPermission(Permission permission) {
        return permissions == null ? new ArrayList<>() : permissions.getAccountIdsWithPermission(permission);
    }

    public boolean hasPermission(String accountId, Permission permission) {
        return permissions != null && permissions.hasPermission(accountId, permission);
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    public void setLastChanged(Long changed) {
        this.changed = changed;
    }

    public void setIndustry(EntityIndustry industry) {
        this.industry = industry;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void replacePermissionSet(String accountId, Set<Permission> permissions) {
        if(this.permissions == null){
            this.permissions = new PermissionsImpl();
        }
        this.permissions.put(accountId, permissions);
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Location getLocation() {
        return location;
    }

    public void setPrimaryRepresentativeUserAccountId(ObjectId primaryRepresentativeUserAccountId) {
        this.primaryRepresentativeUserAccountId = primaryRepresentativeUserAccountId;
    }

    public ObjectId getPrimaryRepresentativeUserAccountId() {
        return primaryRepresentativeUserAccountId;
    }

    @Override
    public Coordinates getCoordinates() {
        return location == null ? null : location.getCoordinates();
    }
}
