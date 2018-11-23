package net.readybid.app.interactors.core.entity;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.location.LocationViewModel;

@SuppressWarnings("WeakerAccess")
public class GetEntityResponse {

    public String id;
    public String type;
    public String name;
    public String industry;
    public String website;
    public String emailAddress;
    public String phone;
    public String logo;
    public String image;
    public LocationViewModel location;
    public String status;

    public GetEntityResponse(Entity entity){
        id = String.valueOf(entity.getId());
        type = entity.getType() == null ? null : entity.getType().toString();
        name = entity.getName();
        industry = entity.getIndustry() == null ? null : entity.getIndustry().toString();;
        website = entity.getWebsite();
        emailAddress = entity.getEmailAddress();
        phone = entity.getPhone();
        logo = entity.getLogo();
        image = entity.getImageUrl();
        location = LocationViewModel.FACTORY.createAsPartial(entity.getLocation());
        status = entity.getStatusValue();
    }
}
