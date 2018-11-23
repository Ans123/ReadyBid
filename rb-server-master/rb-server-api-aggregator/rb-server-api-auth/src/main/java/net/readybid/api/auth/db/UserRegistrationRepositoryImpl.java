package net.readybid.api.auth.db;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.api.auth.registration.UserRegistrationImpl;
import net.readybid.api.auth.registration.UserRegistrationRepository;
import net.readybid.auth.login.GetActiveUserPasswordRepository;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.user.UserStatus;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountRepository;
import net.readybid.mongodb.RbDuplicateKeyException;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byStatusValue;
import static net.readybid.mongodb.RbMongoFilters.include;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository, GetActiveUserPasswordRepository {

    static final String USER_COLLECTION = "User";

    private final MongoCollection<UserRegistrationImpl> userRegistrationCollection;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserRegistrationRepositoryImpl(MongoDatabase mongoDatabase, UserAccountRepository userAccountRepository) {
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
        try {
            userRegistrationCollection.insertOne((UserRegistrationImpl) userRegistration);
            userAccountRepository.create(userRegistration.getCurrentUserAccount());
        } catch (MongoWriteException mwe){
            if (ErrorCategory.DUPLICATE_KEY.equals(mwe.getError().getCategory())) throw new RbDuplicateKeyException(mwe);
        }
    }

    @Override
    public void addUserAccount(UserAccount userAccount, Long lastChangeTimestamp) {
        userAccountRepository.create(userAccount);
        userRegistrationCollection.updateOne(byId(userAccount.getUserId()),
                combine(
                        push("userAccounts", userAccount.getId()),
                        set("currentUserAccountId", userAccount.getId()),
                        set("changed", lastChangeTimestamp)
                )
        );
    }

    private Bson markChange(UserRegistration user) {
        user.markLastChange();
        return set("changed", user.getLastChangeTimestamp());
    }

    @Override
    public String getActiveUserPasswordByEmailAddress(String emailAddress) {
        final UserRegistration user = userRegistrationCollection
                .find(and(eq("emailAddress", emailAddress), byStatusValue(UserStatus.ACTIVE)))
                .projection(include("password"))
                .first();
        return user == null ? null : user.getPassword();
    }
}
