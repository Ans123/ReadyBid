package net.readybid.entities.company.core;

import net.readybid.entities.core.EntityImageImpl;
import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by DejanK on 1/4/2017.
 *
 */
public class CompanyImpl extends EntityImpl implements Company {

    public CompanyImpl(){
        setType(EntityType.COMPANY);
    }

    // todo @PersistenceConstructor
    public CompanyImpl(@Value("location") LocationImpl location, @Value("image") EntityImageImpl image) {
        super(location);
        setType(EntityType.COMPANY);
        setImage(image);
    }
}
