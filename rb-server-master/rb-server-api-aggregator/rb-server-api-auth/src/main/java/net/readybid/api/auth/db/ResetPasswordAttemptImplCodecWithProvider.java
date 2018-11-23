package net.readybid.api.auth.db;

import net.readybid.api.auth.resetpassword.ResetPasswordAttemptImpl;
import net.readybid.api.auth.resetpassword.ResetPasswordAttemptState;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import net.readybid.mongodb.RbMongoDecoder;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class ResetPasswordAttemptImplCodecWithProvider extends RbMongoCodecWithProvider<ResetPasswordAttemptImpl> {

    public ResetPasswordAttemptImplCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(ResetPasswordAttemptImpl.class, bsonTypeClassMap);
    }

    @Override
    protected ResetPasswordAttemptImpl newInstance() {
        return new ResetPasswordAttemptImpl();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<ResetPasswordAttemptImpl>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.setId(reader.readObjectId()));
        decodeMap.put("userId", (c, reader, context) -> c.setUserId(reader.readObjectId()));
        decodeMap.put("username", (c, reader, context) -> c.setUserName(reader.readString()));
        decodeMap.put("emailAddress", (c, reader, context) -> c.setEmailAddress(reader.readString()));
        decodeMap.put("phoneNumber", (c, reader, context) -> c.setPhoneNumber(reader.readString()));
        decodeMap.put("smsCode", (c, reader, context) -> c.setSmsCode(reader.readString()));
        decodeMap.put("attemptsCount", (c, reader, context) -> c.setSmsCodeAttemptsCount(readInt(reader)));
        decodeMap.put("state", (c, reader, context) -> c.setState(read(reader, context, ResetPasswordAttemptState.class)));
        decodeMap.put("expiresAt", (c, reader, context) -> c.setExpiresAt(readLong(reader)));
    }

    @Override
    public void encodeDocument(Document d, ResetPasswordAttemptImpl attempt, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", attempt.getId());
        putIfNotNull(d, "userId", attempt.getUserId());
        putIfNotNull(d, "username", attempt.getUserName());
        putIfNotNull(d, "emailAddress", attempt.getEmailAddress());
        putIfNotNull(d, "phoneNumber", attempt.getPhoneNumber());
        putIfNotNull(d, "smsCode", attempt.getSmsCode());
        putIfNotNull(d, "attemptsCount", attempt.getSmsCodeAttemptsCount());
        putIfNotNull(d, "state", attempt.getState());
        putIfNotNull(d, "expiresAt", attempt.getExpiresAt());
    }
}
