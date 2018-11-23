package net.readybid.entities.core.db;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.core.*;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class EntityImplCodecWithProvider extends RbMongoCodecWithProvider<EntityImpl> {

    public EntityImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(EntityImpl.class, bsonTypeClassMap);
    }

    @Override
    protected EntityImpl newInstance() {
        return new EntityImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<EntityImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, EntityType.class)));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("industry", (c, reader, context) -> c.setIndustry(read(reader, context, EntityIndustry.class)));
        decodeMap.put("website", (c, reader, context) -> c.setWebsite(reader.readString()));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("image", (c, reader, context) -> c.setImage(read(reader, context, EntityImageImpl.class)));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, EntityStatusDetails.class)));
    }

    @Override
    public void encodeDocument(Document d, EntityImpl entity, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", entity.getId());
        putIfNotNull(d, "type", entity.getType());
        putIfNotNull(d, "name", entity.getName());
        putIfNotNull(d, "industry", entity.getIndustry());
        putIfNotNull(d, "website", entity.getWebsite());
        putIfNotNull(d, "location", entity.getLocation());
        putIfNotNull(d, "image", entity.getImage());
        putIfNotNull(d, "created", entity.getCreated());
        putIfNotNull(d, "status", entity.getStatus());
    }
}
