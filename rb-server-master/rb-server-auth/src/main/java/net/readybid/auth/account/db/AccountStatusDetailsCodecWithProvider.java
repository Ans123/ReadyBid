package net.readybid.auth.account.db;


import net.readybid.auth.account.core.AccountStatus;
import net.readybid.auth.account.core.AccountStatusDetails;
import net.readybid.mongodb.AbstractStatusDetailsRbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class AccountStatusDetailsCodecWithProvider extends AbstractStatusDetailsRbMongoCodecWithProvider<AccountStatus, AccountStatusDetails> {

    public AccountStatusDetailsCodecWithProvider(BsonTypeClassMap bsonTypeClassMap) {
        super(bsonTypeClassMap, AccountStatusDetails.class, AccountStatus.class);
    }

    @Override
    protected AccountStatusDetails newInstance() {
        return new AccountStatusDetails();
    }
}