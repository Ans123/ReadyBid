package net.readybid.api.auth.db;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.readybid.api.auth.registration.MockUserRegistrationRepository;
import net.readybid.api.auth.registration.UserRegistrationImpl;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.mongodb.RbDuplicateKeyException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class MockUserRegistrationRepositoryImpl implements MockUserRegistrationRepository {

    static final String USER_COLLECTION = "MockUser";

    private final MongoCollection<UserRegistrationImpl> userRegistrationCollection;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public MockUserRegistrationRepositoryImpl(MongoDatabase mongoDatabase, UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userRegistrationCollection = mongoDatabase.getCollection(USER_COLLECTION, UserRegistrationImpl.class);
    }

    @Override
    // todo: retry
    public UserRegistration findUserRegistration(String emailAddress) {
        return userRegistrationCollection.find(eq("emailAddress", emailAddress)).first();
    }

    @Override
    // todo: retry
    public void createUserRegistration(UserRegistration userRegistration){
        try {
            userRegistrationCollection.insertOne((UserRegistrationImpl) userRegistration);
        } catch (MongoWriteException we){
            throw (ErrorCategory.DUPLICATE_KEY.equals(we.getError().getCategory()) ? new RbDuplicateKeyException(we) : we);
        }
    }

    @Override
    // todo: retry
    public void recreateUserRegistration(UserRegistration userRegistration) {
        userRegistrationCollection.replaceOne(byId(userRegistration.getId()), (UserRegistrationImpl) userRegistration);
    }

    @Override
    // todo: retry
    public void updateUserStatus(UserRegistration userRegistration) {
        userRegistrationCollection.updateOne(
                byId(userRegistration.getId()),
                set("status", userRegistration.getStatus())
        );
    }

    @Override
    public void updatePassword(ObjectId userId, String password) {
        userRegistrationCollection.updateOne(
                byId(userId),
                set("password", password)
        );
    }

    @Override
    public void createUserAndUserAccount(UserRegistration userRegistration) {
        userAccountRepository.create(userRegistration.getCurrentUserAccount());
        try {
            userRegistrationCollection.insertOne((UserRegistrationImpl) userRegistration);
        } catch (MongoWriteException mwe){
            if (ErrorCategory.DUPLICATE_KEY.equals(mwe.getError().getCategory())) throw new RbDuplicateKeyException(mwe);
        }
    }

    @Override
    /**
     * DIRTY dirty hack for demo
     */
    public void createOrUpdateUser(UserRegistration userRegistration) {
        userRegistrationCollection.replaceOne(byId(userRegistration.getId()), (UserRegistrationImpl) userRegistration, new UpdateOptions().upsert(true));
    }

    @Override
    public UserRegistration getFullUserRegistrationByEmail(String emailAddress) {
        return userRegistrationCollection.aggregate(Arrays.asList(
                match(new Document("emailAddress", emailAddress)),
                lookup("UserAccount", "currentUserAccountId", "_id", "currentUserAccount"),
                unwind("$currentUserAccount", true),
                lookup("Account", "currentUserAccount.accountId", "_id", "currentUserAccount.account"),
                unwind("$currentUserAccount.account", true),
                project(
                        new Document("firstName", true)
                                .append("lastName", true)
                                .append("emailAddress", true)
                                .append("password", true)
                                .append("mobile", true)
                                .append("created", true)
                                .append("status", true)
                                .append("changed", true)
                                .append("userAccounts", true)
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

    private Bson markChange(UserRegistration user) {
        user.markLastChange();
        return set("changed", user.getLastChangeTimestamp());
    }
}
