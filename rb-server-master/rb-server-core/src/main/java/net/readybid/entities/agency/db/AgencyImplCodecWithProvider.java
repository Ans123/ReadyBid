package net.readybid.entities.agency.db;

import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
import net.readybid.entities.agency.core.AgencyImpl;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public class AgencyImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<AgencyImpl> {

    public AgencyImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(AgencyImpl.class, bsonTypeClassMap);
    }

    @Override
    protected AgencyImpl newInstance() {
        return new AgencyImpl();
    }
}
