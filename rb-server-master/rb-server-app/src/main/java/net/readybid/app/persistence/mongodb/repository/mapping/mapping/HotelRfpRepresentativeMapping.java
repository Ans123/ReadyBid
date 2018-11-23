package net.readybid.app.persistence.mongodb.repository.mapping.mapping;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;
import org.bson.codecs.BsonTypeClassMap;

import java.util.Map;

public class HotelRfpRepresentativeMapping implements IdField, StatusFields, CreatedFields {

    public static RbMongoCodecWithProvider<HotelRfpRepresentative> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new HotelRfpRepresentativeCodec(bsonTypeClassMap);
    }

    private static class HotelRfpRepresentativeCodec extends RbMongoCodecWithProvider<HotelRfpRepresentative> {

        HotelRfpRepresentativeCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(HotelRfpRepresentative.class, bsonTypeClassMap);
        }

        @Override
        protected HotelRfpRepresentative newInstance() {
            return new HotelRfpRepresentative();
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpRepresentative>> decodeMap) {
            decodeMap.put("_id", (c, reader, context) -> c.id = String.valueOf(reader.readObjectId()));
            decodeMap.put("userId", (c, reader, context) -> c.userId = String.valueOf(reader.readObjectId()));
            decodeMap.put("accountId", (c, reader, context) -> c.accountId = String.valueOf(reader.readObjectId()));
            decodeMap.put("entityId", (c, reader, context) -> c.entityId = String.valueOf(reader.readObjectId()));
            decodeMap.put("accountName", (c, reader, context) -> c.accountName = reader.readString());
            decodeMap.put("accountType", (c, reader, context) -> c.accountType = read(reader, context, EntityType.class));
            decodeMap.put("firstName", (c, reader, context) -> c.firstName = reader.readString());
            decodeMap.put("lastName", (c, reader, context) -> c.lastName = reader.readString());
            decodeMap.put("emailAddress", (c, reader, context) -> c.emailAddress = reader.readString());
            decodeMap.put("phone", (c, reader, context) -> c.phone = reader.readString());
            decodeMap.put("profilePicture", (c, reader, context) -> c.profilePicture = reader.readString());
            decodeMap.put("jobTitle", (c, reader, context) -> c.jobTitle = reader.readString());
            decodeMap.put("isUser", (c, reader, context) -> c.isUser = reader.readBoolean());
        }
    }
}
