package net.readybid.entities.rfp;

import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class EntityRateLoadingInformationImplCodecWithProvider extends RbMongoCodecWithProvider<EntityRateLoadingInformationImpl>{

    public EntityRateLoadingInformationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(EntityRateLoadingInformationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected EntityRateLoadingInformationImpl newInstance() {
        return new EntityRateLoadingInformationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<EntityRateLoadingInformationImpl>> decodeMap) {
        decodeMap.put("entityId", (c, reader, context) -> c.setEntityId(reader.readObjectId()));
        decodeMap.put("entityType", (c, reader, context) -> c.setEntityType(read(reader, context, EntityType.class)));
        decodeMap.put("information", (c, reader, context) -> c.setInformation(readListOfObjects(reader, context, RateLoadingInformationImpl.class)));
        decodeMap.put("entity", (c, reader, context) -> c.setEntity(read(reader, context, EntityImpl.class)));

    }

    @Override
    public void encodeDocument(Document d, EntityRateLoadingInformationImpl entityInformation,
                               BsonWriter bsonWriter, EncoderContext encoderContext
    ) {
        putIfNotNull(d, "entityId", entityInformation.getEntityId());
        putIfNotNull(d, "gdsName", entityInformation.getEntityType());
        putIfNotNull(d, "information", entityInformation.getInformation());
    }
}
