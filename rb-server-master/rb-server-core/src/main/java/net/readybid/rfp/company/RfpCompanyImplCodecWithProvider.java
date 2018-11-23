package net.readybid.rfp.company;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpCompanyImplCodecWithProvider extends RbMongoCodecWithProvider<RfpCompanyImpl> {

    public RfpCompanyImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(RfpCompanyImpl.class, bsonTypeClassMap);
    }

    @Override
    protected RfpCompanyImpl newInstance() {
        return new RfpCompanyImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<RfpCompanyImpl>> decodeMap) {
        decodeMap.put("entityId", (c, reader, context) -> c.setEntityId(reader.readObjectId()));
        decodeMap.put("accountId", (c, reader, context) -> c.setAccountId(reader.readObjectId()));
        decodeMap.put("name", (c, reader, context) -> c.setName(reader.readString()));
        decodeMap.put("type", (c, reader, context) -> c.setType(read(reader, context, EntityType.class)));
        decodeMap.put("industry", (c, reader, context) -> c.setIndustry(read(reader, context, EntityIndustry.class)));
        decodeMap.put("location", (c, reader, context) -> c.setLocation(read(reader, context, LocationImpl.class)));
        decodeMap.put("website", (c, reader, context) -> c.setWebsite(reader.readString()));
        decodeMap.put("logo", (c, reader, context) -> c.setLogo(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
    }

    @Override
    public void encodeDocument(Document d, RfpCompanyImpl company, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "entityId", company.getEntityId());
        putIfNotNull(d, "accountId", company.getAccountId());
        putIfNotNull(d, "name", company.getName());
        putIfNotNull(d, "type", company.getType());
        putIfNotNull(d, "industry", company.getIndustry());
        putIfNotNull(d, "location", company.getLocation());
        putIfNotNull(d, "website", company.getWebsite());
        putIfNotNull(d, "logo", company.getLogo());
        putIfNotNull(d, "emailAddress", company.getEmailAddress());
        putIfNotNull(d, "phone", company.getPhone());
    }
}
