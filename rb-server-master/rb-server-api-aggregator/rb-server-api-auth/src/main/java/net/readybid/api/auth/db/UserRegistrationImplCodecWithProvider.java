package net.readybid.api.auth.db;

import net.readybid.api.auth.registration.UserRegistrationImpl;
import net.readybid.auth.user.UserStatusDetails;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import net.readybid.utils.CreationDetails;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class UserRegistrationImplCodecWithProvider extends RbMongoCodecWithProvider<UserRegistrationImpl> {

    public UserRegistrationImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(UserRegistrationImpl.class, bsonTypeClassMap);
    }

    @Override
    protected UserRegistrationImpl newInstance() {
        return new UserRegistrationImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<UserRegistrationImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("firstName", (c, reader, context) -> c.setFirstName(reader.readString()));
        decodeMap.put("lastName", (c, reader, context) -> c.setLastName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("password", (c, reader, context) -> c.setPassword(reader.readString()));
        decodeMap.put("phone", (c, reader, context) -> c.setPhone(reader.readString()));
        decodeMap.put("tutorials", (c, reader, context) -> c.setTutorials(readList(reader, String.class)));

        decodeMap.put("userAccounts", (c, reader, context) -> c.setUserAccounts(readObjectIdSet(reader)));
        decodeMap.put("currentUserAccountId", (c, reader, context) -> c.setCurrentUserAccountId(reader.readObjectId()));
        decodeMap.put("currentUserAccount", (c, reader, context) -> c.setCurrentUserAccount(read(reader, context, UserAccountImpl.class)));

        decodeMap.put("created", (c, reader, context) -> c.setCreated(read(reader, context, CreationDetails.class)));

        decodeMap.put("status", (c, reader, context) -> c.setStatus(read(reader, context, UserStatusDetails.class)));
        decodeMap.put("changed", (c, reader, context) -> c.setLastChangeTimestamp(readLong(reader)));
    }

    @Override
    public void encodeDocument(Document d, UserRegistrationImpl userRegistration, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", userRegistration.getId());
        putIfNotNull(d, "firstName", userRegistration.getFirstName());
        putIfNotNull(d, "lastName", userRegistration.getLastName());
        putIfNotNull(d, "emailAddress", userRegistration.getEmailAddress());
        putIfNotNull(d, "password", userRegistration.getPassword());
        putIfNotNull(d, "phone", userRegistration.getPhone());
        putIfNotNull(d, "tutorials", userRegistration.getTutorials());

        putIfNotNull(d, "userAccounts", userRegistration.getUserAccounts());
        putIfNotNull(d, "currentUserAccountId", userRegistration.getCurrentUserAccountId());

        putIfNotNull(d, "created", userRegistration.getCreated());
        putIfNotNull(d, "status", userRegistration.getStatus());
        putIfNotNull(d, "changed", userRegistration.getLastChangeTimestamp());
    }
}
