package net.readybid.entities.consultancy.core;

import net.readybid.entities.core.EntityImageImpl;
import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class ConsultancyImpl extends EntityImpl implements Consultancy {

    public ConsultancyImpl(){
        setType(EntityType.TRAVEL_CONSULTANCY);
    }

    // todo @PersistenceConstructor
    public ConsultancyImpl(@Value("location") LocationImpl location, @Value("image") EntityImageImpl image) {
        super(location);
        setType(EntityType.COMPANY);
        setImage(image);
    }
}
