package net.readybid.auth.user;

import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class
        AuthenticatedUserImplCodecWithProvider extends RbMongoCodecWithProvider<AuthenticatedUserImpl> {

    public AuthenticatedUserImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(AuthenticatedUserImpl.class, bsonTypeClassMap);
    }

    @Override
    protected AuthenticatedUserImpl newInstance() {
        return new AuthenticatedUserImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<AuthenticatedUserImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("profilePicture", (c, reader, context) -> c.setProfilePicture(reader.readString()));
        decodeMap.put("tutorials", (c, reader, context) -> c.setTutorials(readList(reader, String.class)));

        decodeMap.put("userAccounts", (c, reader, context) -> c.setUserAccounts(readSet(reader, ObjectId.class)));
        decodeMap.put("currentUserAccountId", (c, reader, context) -> c.setCurrentUserAccountId(reader.readObjectId()));
        decodeMap.put("currentUserAccount", (c, reader, context) -> c.setCurrentUserAccount(read(reader, context, UserAccountImpl.class)));

        decodeMap.put("changed", (c, reader, context) -> c.setLastChangeTimestamp(readLongObject(reader)));

        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));
        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, UserStatusDetails.class)));
    }

    @Override
    public void encodeDocument(Document d, AuthenticatedUserImpl user, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", user.getId());
        putIfNotNull(d, "firstName", user.getFirstName());
        putIfNotNull(d, "lastName", user.getLastName());
        putIfNotNull(d, "emailAddress", user.getEmailAddress());
        putIfNotNull(d, "phone", user.getPhone());
        putIfNotNull(d, "tutorials", user.getTutorials());
        putIfNotNull(d, "profilePicture", user.getProfilePicture());

        putIfNotNull(d, "userAccounts", user.getUserAccounts());
        putIfNotNull(d, "currentUserAccountId", user.getCurrentUserAccountId());

        putIfNotNull(d, "changed", user.getLastChangeTimestamp());

        putIfNotNull(d, "created", user.getCreated());
        putIfNotNull(d, "status", user.getStatus());
    }
}
