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
 * Created by DejanK on 1/8/2017.
 *
 */
public abstract class AbstractEntityRbMongoCodecWithProvider<T extends EntityImpl> extends RbMongoCodecWithProvider<T> {

    public AbstractEntityRbMongoCodecWithProvider(Class<T> tClass, BsonTypeClassMap bsonTypeClassMap) {
        super(tClass, bsonTypeClassMap);
    }

    public void decodeMapSetup(Map<String, RbMongoDecoder<T>> decodeMap){
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, EntityType.class)));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("industry", (c, reader, context) -> c.setIndustry(read(reader, context, EntityIndustry.class)));
        decodeMap.put("website", (c, reader, context) -> c.setWebsite(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("logo", (c, reader, context) -> c.setLogo(reader.readString()));
        decodeMap.put("image", (c, reader, context) -> c.setImage(read(reader, context, EntityImageImpl.class)));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, EntityStatusDetails.class)));
    }

    public void encodeDocument(Document d, T t, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", t.getId());
        putIfNotNull(d, "type", t.getType());
        putIfNotNull(d, "name", t.getName());
        putIfNotNull(d, "industry", t.getIndustry());
        putIfNotNull(d, "website", t.getWebsite());
        putIfNotNull(d, "emailAddress", t.getEmailAddress());
        putIfNotNull(d, "phone", t.getPhone());
        putIfNotNull(d, "logo", t.getLogo());
        putIfNotNull(d, "image", t.getImage());
        putIfNotNull(d, "location", t.getLocation());
        putIfNotNull(d, "created", t.getCreated());
        putIfNotNull(d, "status", t.getStatus());
    }
}
