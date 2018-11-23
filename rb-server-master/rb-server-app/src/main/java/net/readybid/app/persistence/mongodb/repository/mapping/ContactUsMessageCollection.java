package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class ContactUsMessageCollection {

    public static final String COLLECTION_NAME = "ContactUsMessage";

    public static final String NAME = "name";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String MESSAGE = "message";
    public static final String CREATED_AT = "createdAt";

    public static RbMongoCodecWithProvider<ContactUsMessage> codec(BsonTypeClassMap bsonTypeClassMap) {
        return new ContactUsMessageCodec(bsonTypeClassMap);
    }

    private static class ContactUsMessageCodec extends RbMongoCodecWithProvider<ContactUsMessage> {

        ContactUsMessageCodec(BsonTypeClassMap bsonTypeClassMap) {
            super(ContactUsMessage.class, bsonTypeClassMap);
        }

        @Override
        protected ContactUsMessage newInstance() {
            return null;
        }

        @Override
        public void decodeMapSetup(Map<String, RbMongoDecoder<ContactUsMessage>> decodeMap) {}

        @Override
        public void encodeDocument(Document document, ContactUsMessage tObject, BsonWriter bsonWriter, EncoderContext encoderContext) {
            document.put(NAME, tObject.name);
            document.put(EMAIL_ADDRESS, tObject.emailAddress);
            document.put(MESSAGE, tObject.message);
            document.put(CREATED_AT, tObject.createdAt);
        }
    }
}
