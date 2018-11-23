package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class CustomRfpMessageCollection {

    public static final String COLLECTION_NAME = "CustomRfpMessage";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String PHONE = "phone";
    public static final String COMPANY = "company";
    public static final String MESSAGE = "message";
    public static final String CREATED_AT = "createdAt";

    public static RbMongoCodecWithProvider<CustomRfpMessage> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new CustomRfpMessageCodec(bsonTypeClassMap);
    }

    private static class CustomRfpMessageCodec extends RbMongoCodecWithProvider<CustomRfpMessage> {

        CustomRfpMessageCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(CustomRfpMessage.class, bsonTypeClassMap);
        }

        @Override
        protected CustomRfpMessage newInstance() {
            return null;
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<CustomRfpMessage>> decodeMap) {}

        @Override
        public void encodeDocument(Document document, CustomRfpMessage tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
            document.put(FIRST_NAME, tObject.firstName);
            document.put(LAST_NAME, tObject.lastName);
            document.put(EMAIL_ADDRESS, tObject.emailAddress);
            document.put(PHONE, tObject.phone);
            document.put(COMPANY, tObject.company);
            document.put(MESSAGE, tObject.message);
            document.put(CREATED_AT, tObject.createdAt);
        }
    }
}