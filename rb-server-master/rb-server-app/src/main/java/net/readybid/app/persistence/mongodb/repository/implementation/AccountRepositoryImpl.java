package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.persistence.mongodb.repository.AccountRepository;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection.COLLECTION_NAME;
import static net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection.CHANGED;

@Repository("NewAccountRepositoryImpl")
public class AccountRepositoryImpl implements AccountRepository {

    private final MongoCollection<AccountImpl> accountCollection;

    public AccountRepositoryImpl(MongoDatabase database) {
        this.accountCollection = database.getCollection(COLLECTION_NAME, AccountImpl.class);
    }

    @Override
    @MongoRetry
    public AccountImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        return accountCollection.findOneAndUpdate(query, markChanged(update), options);
    }

    @Override
    @MongoRetry
    public Account findOne(Bson filter, Bson fields) {
        return accountCollection.find(filter).first();
    }

    private Bson markChanged(Bson update){
        return combine(update,set(CHANGED, new Date().getTime()));
    }
}