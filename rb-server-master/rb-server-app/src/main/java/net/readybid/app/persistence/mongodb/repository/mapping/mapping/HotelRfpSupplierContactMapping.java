package net.readybid.app.persistence.mongodb.repository.mapping.mapping;

import net.readybid.app.entities.rfp_hotel.HotelRfpContactAccount;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.entities.Id;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class HotelRfpSupplierContactMapping {

    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String PHONE = "phone";
    public static final String JOB_TITLE = "jobTitle";
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String COMPANY = "company";
    public static final String IS_USER = "isUser";

    public static RbMongoCodecWithProvider<HotelRfpSupplierContact> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new HotelRfpSupplierContactCodec(bsonTypeClassMap);
    }

    private static class HotelRfpSupplierContactCodec extends RbMongoCodecWithProvider<HotelRfpSupplierContact> {

        HotelRfpSupplierContactCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(HotelRfpSupplierContact.class, bsonTypeClassMap);
        }

        @Override
        public HotelRfpSupplierContact newInstance() {
            return new HotelRfpSupplierContact();
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<HotelRfpSupplierContact>> decodeMap) {
            decodeMap.put(ID, (c, reader, context) -> c.id = read(reader, context, Id.class));
            decodeMap.put(FIRST_NAME, (c, reader, context) -> c.firstName = reader.readString());
            decodeMap.put(LAST_NAME, (c, reader, context) -> c.lastName = reader.readString());
            decodeMap.put(EMAIL_ADDRESS, (c, reader, context) -> c.emailAddress = reader.readString());
            decodeMap.put(PHONE, (c, reader, context) -> c.phone = reader.readString());
            decodeMap.put(JOB_TITLE, (c, reader, context) -> c.jobTitle = reader.readString());
            decodeMap.put(PROFILE_PICTURE, (c, reader, context) -> c.profilePicture = reader.readString());
            decodeMap.put(COMPANY, (c, reader, context) -> c.company = read(reader, context, HotelRfpContactAccount.class));
            decodeMap.put(IS_USER, (c, reader, context) -> c.isUser = reader.readBoolean());
        }

        @Override
        public void encodeDocument(Document d, HotelRfpSupplierContact o, BsonWriter bsonWriter, EncoderContext encoderContext){
            putIfNotNull(d, ID, o.id);
            putIfNotNull(d, FIRST_NAME, o.firstName);
            putIfNotNull(d, LAST_NAME, o.lastName);
            putIfNotNull(d, FULL_NAME, o.getFullName());
            putIfNotNull(d, EMAIL_ADDRESS, o.emailAddress);
            putIfNotNull(d, PHONE, o.phone);
            putIfNotNull(d, JOB_TITLE, o.jobTitle);
            putIfNotNull(d, PROFILE_PICTURE, o.profilePicture);
            putIfNotNull(d, COMPANY, o.company);
            putIfNotNull(d, IS_USER, o.isUser);
        }
    }
}
