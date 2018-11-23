package net.readybid.entities.agency.core;

import net.readybid.entities.core.EntityImageImpl;
import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
// import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public class AgencyImpl extends EntityImpl implements Agency {

    public AgencyImpl(){
        setType(EntityType.TRAVEL_AGENCY);
    }

//   todo @PersistenceConstructor
//    public AgencyImpl(@Value("location") LocationImpl location, @Value("image") EntityImageImpl image) {
    public AgencyImpl(LocationImpl location, EntityImageImpl image) {
        super(location);
        setType(EntityType.COMPANY);
        setImage(image);
    }
}
