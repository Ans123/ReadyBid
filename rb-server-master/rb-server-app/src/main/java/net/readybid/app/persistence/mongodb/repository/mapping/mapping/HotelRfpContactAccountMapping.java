package net.readybid.app.persistence.mongodb.repository.mapping.mapping;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.app.entities.rfp_hotel.HotelRfpContactAccount;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class HotelRfpContactAccountMapping {

    public static final String TYPE = "type";
    public static final String ACCOUNT_ID = "accountId";
    public static final String ENTITY_ID = "entityId";
    public static final String NAME = "name";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String PHONE = "phone";
    public static final String WEBSITE = "website";
    public static final String LOGO = "logo";
    public static final String LOCATION = "location";

    public static RbMongoCodecWithProvider<HotelRfpContactAccount> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new HotelRfpContactAccountCodec(bsonTypeClassMap);
    }

    private static class HotelRfpContactAccountCodec extends RbMongoCodecWithProvider<HotelRfpContactAccount> {

        HotelRfpContactAccountCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(HotelRfpContactAccount.class, bsonTypeClassMap);
        }

        @Override
        protected HotelRfpContactAccount newInstance() {
            return new HotelRfpContactAccount();
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpContactAccount>> decodeMap) {
            decodeMap.put(TYPE, (c, reader, context) -> c.type = read(reader, context, EntityType.class));
            decodeMap.put(ACCOUNT_ID, (c, reader, context) -> c.accountId = read(reader, context, Id.class));
            decodeMap.put(ENTITY_ID, (c, reader, context) -> c.entityId = read(reader, context, Id.class));
            decodeMap.put(NAME, (c, reader, context) -> c.name = reader.readString());
            decodeMap.put(EMAIL_ADDRESS, (c, reader, context) -> c.emailAddress = reader.readString());
            decodeMap.put(PHONE, (c, reader, context) -> c.phone = reader.readString());
            decodeMap.put(WEBSITE, (c, reader, context) -> c.website = reader.readString());
            decodeMap.put(LOGO, (c, reader, context) -> c.logo = reader.readString());
            decodeMap.put(LOCATION, (c, reader, context) -> c.location = read(reader, context, LocationImpl.class));
        }

        @Override
        public void encodeDocument(Document d, HotelRfpContactAccount o, BsonWriter bsonWriter, EncoderContext encoderContext){
            putIfNotNull(d, TYPE, o.type);
            putIfNotNull(d, ACCOUNT_ID, o.accountId);
            putIfNotNull(d, ENTITY_ID, o.entityId);
            putIfNotNull(d, NAME, o.name);
            putIfNotNull(d, EMAIL_ADDRESS, o.emailAddress);
            putIfNotNull(d, PHONE, o.phone);
            putIfNotNull(d, WEBSITE, o.website);
            putIfNotNull(d, LOGO, o.logo);
            putIfNotNull(d, LOCATION, o.location);
        }
    }
}
