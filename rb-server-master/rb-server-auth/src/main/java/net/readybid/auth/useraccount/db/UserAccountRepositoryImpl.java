package net.readybid.auth.useraccount.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.auth.useraccount.web.UserAccountViewModel;
import net.readybid.mongodb.MongoRetry;
import net.readybid.utils.ListResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
@Service
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final MongoCollection<UserAccountImpl> userAccountCollection;

    @Autowired
    public UserAccountRepositoryImpl(MongoDatabase mongoDatabase) {
        userAccountCollection = mongoDatabase.getCollection(UserAccountRepository.COLLECTION, UserAccountImpl.class);
    }

    @Override
    @MongoRetry
    public void create(UserAccount userAccount) {
        userAccountCollection.insertOne((UserAccountImpl) userAccount);
    }

    @Override
    @MongoRetry
    public UserAccount getById(ObjectId userAccountId) {
        return userAccountCollection.aggregate(
                Arrays.asList(
                        match(new Document("_id", userAccountId)),
                        lookup("Account", "accountId", "_id", "account"),
                        unwind("$account"),
                        lookup("User", "userId", "_id", "user"),
                        unwind("$user")
                )).first();
    }

    @Override
    public UserAccount getById(String id) {
        return id != null && ObjectId.isValid(id) ? getById(new ObjectId(id)) : null;
    }

    @Override
    @MongoRetry
    public void updateLastUsedBidManager(UserAccount account) {

        userAccountCollection.updateOne(
                byId(account.getId()),
                combine(
                        set("lastBmView", account.getLastUsedBidManagerView()),
                        markChange(account)
                )
        );
    }

    @Override
    @MongoRetry
    public List<? extends UserAccount> getUserAccountsByAccountId(ObjectId accountId) {
        return userAccountCollection.aggregate(
                Arrays.asList(
                        match(new Document("accountId", accountId)),
                        lookup("Account", "accountId", "_id", "account"),
                        unwind("$account"),
                        lookup("User", "userId", "_id", "user"),
                        unwind("$user")
                )).into(new ArrayList<>());
    }

    @Override
    @MongoRetry
    public UserAccount getUserAccountByUserIdAndAccountId(ObjectId userId, ObjectId accountId) {
        return userAccountCollection.find(and(eq("userId", userId), eq("accountId", accountId))).first();
    }

    @Override
    public ListResult<UserAccountViewModel> listUserAccountsForUser(AuthenticatedUser currentUser) {
        final List<? extends UserAccount> userAccounts =  userAccountCollection.aggregate(
                Arrays.asList(
                        new Document("$match", new Document("_id", new Document("$in", currentUser.getUserAccounts()))),
                        lookup("Account", "accountId", "_id", "account"),
                        unwind("$account")
                )).into(new ArrayList<>());

        return new ListResult<>(UserAccountViewModel.FACTORY.createList(userAccounts));
    }

    private Bson markChange(UserAccount userAccount) {
        userAccount.markLastChange();
        return set("changed", userAccount.getLastChangeTimestamp());
    }
}
