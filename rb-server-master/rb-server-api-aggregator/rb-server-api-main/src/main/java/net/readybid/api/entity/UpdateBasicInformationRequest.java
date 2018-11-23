package net.readybid.api.entity;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import net.readybid.location.LocationTO;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class UpdateBasicInformationRequest {

    @NotBlank
    @Size(max = 100)
    public String name;

    @NotNull
    @Valid
    public EntityType type;

    @NotNull
    @Valid
    public EntityIndustry industry;

    @URL
    public String website;

    @Valid
    @NotNull
    public LocationTO location;

    public EntityBasicInformationUpdateRequest toEntityBasicInformationUpdateRequest() {
        return new EntityBasicInformationUpdateRequest(name, industry, website, location.load());
    }
}
