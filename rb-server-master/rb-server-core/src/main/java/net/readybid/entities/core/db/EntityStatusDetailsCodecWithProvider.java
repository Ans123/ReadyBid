package net.readybid.entities.core.db;


import net.readybid.entities.core.EntityStatus;
import net.readybid.entities.core.EntityStatusDetails;
import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class EntityStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<EntityStatus, EntityStatusDetails> {

    public EntityStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, EntityStatusDetails.class, EntityStatus.class);
    }

    @Override
    protected EntityStatusDetails newInstance() {
        return new EntityStatusDetails();
    }
}