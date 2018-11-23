package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.location.LocationViewModel;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public abstract class EntityView {

    public String id;
    public EntityType type;
    public String name;
    public EntityIndustry industry;
    public String website;
    public String emailAddress;
    public String phone;
    public String logo;
    public EntityImageView image;
    public LocationViewModel location;

    public EntityView(Entity entity) {
        id = String.valueOf(entity.getId());
        type = entity.getType();
        name = entity.getName();
        industry = entity.getIndustry();
        website = entity.getWebsite();
        emailAddress = entity.getEmailAddress();
        phone = entity.getPhone();
        logo = entity.getLogo();
        image = EntityImageView.FACTORY.createAsPartial(entity.getImage());
        location = LocationViewModel.FACTORY.createAsPartial(entity.getLocation());
    }

    public EntityView() {}
}
