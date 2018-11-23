package net.readybid.entities.company.db;

import net.readybid.entities.company.core.CompanyImpl;
import net.readybid.entities.core.db.AbstractEntityRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class CompanyImplCodecWithProvider extends AbstractEntityRbMongoCodecWithProvider<CompanyImpl> {

    public CompanyImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(CompanyImpl.class, bsonTypeClassMap);
    }

    @Override
    protected CompanyImpl newInstance() {
        return new CompanyImpl();
    }
}
