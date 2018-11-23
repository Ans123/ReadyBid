package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.auth.account.core.Account;
import org.bson.conversions.Bson;

public interface AccountRepository {
    Account findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options);

    Account findOne(Bson filter, Bson fields);
}
