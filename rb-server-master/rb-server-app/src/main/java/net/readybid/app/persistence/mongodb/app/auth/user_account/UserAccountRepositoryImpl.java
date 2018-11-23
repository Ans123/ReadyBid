package net.readybid.app.persistence.mongodb.app.auth.user_account;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.persistence.mongodb.repository.UserAccountRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.UserAccountCollection;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("NewUserAccountRepository")
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final MongoCollection<UserAccountImpl> collection;

    public UserAccountRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection(UserAccountCollection.COLLECTION_NAME, UserAccountImpl.class);
    }

    @Override
    @MongoRetry
    public List<? extends UserAccount> find(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).into(new ArrayList<>());
    }
}
