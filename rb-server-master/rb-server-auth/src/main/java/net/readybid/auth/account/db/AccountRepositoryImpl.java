package net.readybid.auth.account.db;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountImpl;
import net.readybid.auth.account.core.AccountRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.eq;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
@Service
public class AccountRepositoryImpl implements AccountRepository {

    private final MongoCollection<AccountImpl> accountCollection;

    @Autowired
    public AccountRepositoryImpl(MongoDatabase mongoDatabase){
        accountCollection = mongoDatabase.getCollection("Account", AccountImpl.class);
    }

    @Override
    @MongoRetry
    public void createAccountForEntity(Account account) {
        accountCollection.insertOne((AccountImpl) account);
    }

    @Override
    @MongoRetry
    public Account getById(ObjectId accountId) {
        return getAccount(byId(accountId)).first();
    }

    @Override
    @MongoRetry
    public Account getAccountByEntityId(ObjectId id) {
        return getAccount(eq("entityId", id)).first();
    }

    @Override
    @MongoRetry
    public Account getAccountByEntityId(String id) {
        return getAccountByEntityId(oid(id));
    }

    private AggregateIterable<AccountImpl> getAccount(Bson query){
        return accountCollection.aggregate(joinCreatedAndStatus(query));
    }
}
