package net.readybid.auth.user;

import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
public class UserImplCodecWithProvider extends RbMongoCodecWithProvider<UserImpl> {

    public UserImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(UserImpl.class, bsonTypeClassMap);
    }

    @Override
    protected UserImpl newInstance() {
        return new UserImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<UserImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("profilePicture", (c, reader, context) -> c.setProfilePicture(reader.readString()));
        decodeMap.put("tutorials", (c, reader, context) -> c.setTutorials(readList(reader, String.class)));

        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, UserStatusDetails.class)));
    }

    @Override
    public void encodeDocument(Document d, UserImpl user, BsonWriter bsonWriter, EncoderContext encoderContext) {
        // not used
    }
}
