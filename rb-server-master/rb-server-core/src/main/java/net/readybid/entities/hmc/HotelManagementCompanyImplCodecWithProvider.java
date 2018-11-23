package net.readybid.entities.hmc;

import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class HotelManagementCompanyImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<HotelManagementCompanyImpl> {

    public HotelManagementCompanyImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(HotelManagementCompanyImpl.class, bsonTypeClassMap);
    }

    @Override
    protected HotelManagementCompanyImpl newInstance() {
        return new HotelManagementCompanyImpl();
    }
}
