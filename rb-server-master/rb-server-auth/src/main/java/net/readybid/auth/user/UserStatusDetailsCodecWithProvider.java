package net.readybid.auth.user;


import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public class UserStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<UserStatus, UserStatusDetails> {

    public UserStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, UserStatusDetails.class, UserStatus.class);
    }

    @Override
    protected UserStatusDetails newInstance() {
        return new UserStatusDetails();
    }
}