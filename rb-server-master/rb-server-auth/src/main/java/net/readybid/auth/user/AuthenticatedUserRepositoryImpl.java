package net.readybid.auth.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
@Service
public class AuthenticatedUserRepositoryImpl implements AuthenticatedUserRepository {

    private final MongoCollection<AuthenticatedUserImpl> userCollection;

    public AuthenticatedUserRepositoryImpl(MongoDatabase mongoDatabase) {
        this.userCollection = mongoDatabase.getCollection("User", AuthenticatedUserImpl.class);
    }

    @Override
    @MongoRetry
    public AuthenticatedUserImpl get(ObjectId userId) {
        final AuthenticatedUserImpl user =  userCollection.aggregate(
                Arrays.asList(
                        new Document("$match", new Document("_id", userId)),
                        new Document("$lookup", new Document()
                                .append("from", "UserAccount")
                                .append("localField", "currentUserAccountId")
                                .append("foreignField", "_id")
                                .append("as", "currentUserAccount")
                        ),
                        new Document("$unwind", new Document()
                                .append("path", "$currentUserAccount")
                                .append("preserveNullAndEmptyArrays", true)
                        ),
                        new Document("$lookup", new Document()
                                .append("from", "Account")
                                .append("localField", "currentUserAccount.accountId")
                                .append("foreignField", "_id")
                                .append("as", "currentUserAccount.account")
                        ),
                        new Document("$unwind", new Document()
                                .append("path", "$currentUserAccount.account")
                                .append("preserveNullAndEmptyArrays", true)
                        )
                )).first();
        if(user != null){
            if(user.getCurrentUserAccountId() == null){
                user.setCurrentUserAccount(null);
            } else if(user.getCurrentUserAccount() != null){
                ((UserAccountImpl) user.getCurrentUserAccount()).setUser(user);
            }
        }
        return user;
    }

    @Override
    @MongoRetry
    public void addUserAccount(AuthenticatedUser user, ObjectId userAccountId) {
        userCollection.updateOne(
                byId(user.getId()),
                combine(
                        addToSet("userAccounts", userAccountId),
                        markChange(user)
                )
        );
    }

    @Override
    @MongoRetry
    public void setCurrentUserAccount(AuthenticatedUser user) {
        userCollection.updateOne(
                byId(user.getId()),
                combine(
                        set("currentUserAccountId", user.getCurrentUserAccountId()),
                        markChange(user)
                )
        );
    }

    @Override
    @MongoRetry
    public void create(AuthenticatedUser authenticatedUser) {
        userCollection.insertOne((AuthenticatedUserImpl) authenticatedUser);
    }

    @Override
    @MongoRetry
    public AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, ObjectId accountId) {
        return userCollection.aggregate(
                Arrays.asList(
                        match(new Document("emailAddress", emailAddress)),
                        lookup("UserAccount", "_id", "userId", "currentUserAccount"),
                        project(
                                new Document("firstName", true)
                                        .append("lastName", true)
                                        .append("emailAddress", true)
                                        .append("mobile", true)
                                        .append("created", true)
                                        .append("status", true)
                                        .append("changed", true)
                                        .append("currentUserAccount", new Document("$filter",
                                                new Document("input", "$currentUserAccount")
                                                        .append("as", "currentUserAccount")
                                                        .append("cond", new Document("$eq", Arrays.asList("$$currentUserAccount.accountId", accountId)))
                                        ))
                        ),
                        unwind("$currentUserAccount", true),
                        lookup("Account", "currentUserAccount.accountId", "_id", "currentUserAccount.account"),
                        unwind("$currentUserAccount.account", true),
                        project(
                                new Document("firstName", true)
                                        .append("lastName", true)
                                        .append("emailAddress", true)
                                        .append("mobile", true)
                                        .append("created", true)
                                        .append("status", true)
                                        .append("changed", true)
                                        .append("currentUserAccount", new Document("$cond", Arrays.asList(
                                                        new Document("$eq", Arrays.asList(
                                                                new Document("$ifNull", Arrays.asList("$currentUserAccount._id", true)),
                                                                true
                                                        )),
                                                        null,
                                                        "$currentUserAccount"
                                                ))
                                        )
                        )
                )).first();
    }

    @Override
    public AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, String accountId) {
        return getUserByEmailAddressAndAccountId(emailAddress, oid(accountId));
    }

    @Override
    @MongoRetry
    public void deleteTutorial(AuthenticatedUser user, String tutorialName) {
        userCollection.updateOne(byId(user.getId()),
                combine(
                        pull("tutorials", tutorialName),
                        markChange(user)
                ));
    }

    @Override
    @MongoRetry
    public boolean isUser(String emailAddress) {
        final long count = userCollection.count(eq("emailAddress", emailAddress));
        return 1 == count;
    }

    private Bson markChange(AuthenticatedUser user) {
        user.markLastChange();
        return set("changed", user.getLastChangeTimestamp());
    }
}
