package net.readybid.auth.login;

import net.readybid.auth.login.LoginAttempt;
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
public class LoginAttemptCodecWithProvider extends RbMongoCodecWithProvider<LoginAttempt> {

    public LoginAttemptCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(LoginAttempt.class, bsonTypeClassMap);
    }

    @Override
    protected LoginAttempt newInstance() {
        return new LoginAttempt();
    }

    @Override
    public void decodeMapSetup(Map<String, RbMongoDecoder<LoginAttempt>> decodeMap) {
        decodeMap.put("_id", (c, reader, context) -> c.id = reader.readObjectId());
        decodeMap.put("target", (c, reader, context) -> c.target = reader.readString());
        decodeMap.put("at", (c, reader, context) -> c.at = readLong(reader));
    }

    @Override
    public void encodeDocument(Document d, LoginAttempt attempt, BsonWriter bsonWriter, EncoderContext encoderContext) {
        putIfNotNull(d, "_id", attempt.id);
        putIfNotNull(d, "target", attempt.target);
        putIfNotNull(d, "at", attempt.at);
    }
}
