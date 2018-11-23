package net.readybid.entities.consultancy.db;

import net.readybid.entities.consultancy.core.ConsultancyImpl;
import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class ConsultancyImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<ConsultancyImpl> {

    public ConsultancyImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(ConsultancyImpl.class, bsonTypeClassMap);
    }

    @Override
    protected ConsultancyImpl newInstance() {
        return new ConsultancyImpl();
    }
}
