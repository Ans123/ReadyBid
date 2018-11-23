package net.readybid.auth.useraccount;


import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class UserAccountStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<UserAccountStatus, UserAccountStatusDetails> {

    public UserAccountStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, UserAccountStatusDetails.class, UserAccountStatus.class);
    }

    @Override
    protected UserAccountStatusDetails newInstance() {
        return new UserAccountStatusDetails();
    }
}