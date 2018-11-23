package net.readybid.app.use_cases.core.entity;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.location.Location;
import net.readybid.utils.Utils;

public class EntityBasicInformationUpdateRequest {

    public final String name;
    public final EntityIndustry industry;
    public final String website;
    public final Location location;

    public EntityBasicInformationUpdateRequest(String name, EntityIndustry industry, String website, Location location) {
        this.name = name;
        this.industry = industry;
        this.website = website;
        this.location = location;
    }

    public boolean hasDifferences(Entity entity) {
        if(entity == null) return false;
        boolean hasDifferences = !Utils.areEqual(name, entity.getName());
        hasDifferences = hasDifferences || !Utils.areEqual(industry, entity.getIndustry());
        hasDifferences = hasDifferences || !Utils.areEqual(website, entity.getWebsite());
        hasDifferences = hasDifferences || !Utils.areEqual(location, entity.getLocation());
        return hasDifferences;
    }
}
