package net.readybid.user;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class BasicUserDetailsImplCodecWithProvider extends RbMongoCodecWithProvider<BasicUserDetailsImpl> {

    public BasicUserDetailsImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(BasicUserDetailsImpl.class, bsonTypeClassMap);
    }

    @Override
    public Class<BasicUserDetailsImpl> getEncoderClass() {
        return BasicUserDetailsImpl.class;
    }

    @Override
    protected BasicUserDetailsImpl newInstance() {
        return new BasicUserDetailsImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<BasicUserDetailsImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
    }

    @Override
    public void encodeDocument(Document d, BasicUserDetailsImpl user, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", user.getId());
        putIfNotNull(d, "firstName", user.getFirstName());
        putIfNotNull(d, "lastName", user.getLastName());
        putIfNotNull(d, "emailAddress", user.getEmailAddress());
        putIfNotNull(d, "phone", user.getPhone());
    }
}
