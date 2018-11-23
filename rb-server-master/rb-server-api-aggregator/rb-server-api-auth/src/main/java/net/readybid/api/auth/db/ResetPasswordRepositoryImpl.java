package net.readybid.api.auth.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.api.auth.registration.UserRegistrationRepository;
import net.readybid.api.auth.resetpassword.ResetPasswordAttempt;
import net.readybid.api.auth.resetpassword.ResetPasswordAttemptImpl;
import net.readybid.api.auth.resetpassword.ResetPasswordRepository;
import net.readybid.auth.user.UserRegistration;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.byId;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordRepositoryImpl implements ResetPasswordRepository {

    static final String RESET_PASSWORD_ATTEMPT_COLLECTION = "ResetPasswordAttempt";

    private final UserRegistrationRepository userRegistrationRepository;
    private final MongoCollection<ResetPasswordAttemptImpl> resetPasswordAttemptCollection;

    @Autowired
    public ResetPasswordRepositoryImpl(UserRegistrationRepository userRegistrationRepository, MongoDatabase mongoDatabase) {
        this.userRegistrationRepository = userRegistrationRepository;
        resetPasswordAttemptCollection = mongoDatabase.getCollection(RESET_PASSWORD_ATTEMPT_COLLECTION, ResetPasswordAttemptImpl.class);
    }

    @Override
    public void create(ResetPasswordAttempt resetPasswordAttempt) {
        resetPasswordAttemptCollection.insertOne((ResetPasswordAttemptImpl) resetPasswordAttempt);
    }

    @Override
    public ResetPasswordAttempt findById(String resetPasswordAttemptId) {
        return resetPasswordAttemptCollection.find(byId(resetPasswordAttemptId)).first();
    }

    public ResetPasswordAttempt findById(ObjectId resetPasswordAttemptId) {
        return resetPasswordAttemptCollection.find(byId(resetPasswordAttemptId)).first();
    }

    @Override
    public void update(ResetPasswordAttempt attempt) {
        resetPasswordAttemptCollection.replaceOne(byId(attempt.getId()), (ResetPasswordAttemptImpl) attempt);
    }

    @Override
    public UserRegistration findUserRegistration(String emailAddress) {
        return userRegistrationRepository.findUserRegistration(emailAddress);
    }

    @Override
    public void updatePassword(ObjectId userId, String password) {
        userRegistrationRepository.updatePassword(userId, password);
    }

    @Override
    public void delete(ResetPasswordAttempt attempt) {
        resetPasswordAttemptCollection.deleteOne(byId(attempt.getId()));
        deleteExpiredAttempts();
    }

    private void deleteExpiredAttempts() {
        resetPasswordAttemptCollection.deleteMany(
                lt("expiresAt", new Date().getTime())
        );
    }

    @Override
    public int increaseSmsCodeAttemptsCount(ResetPasswordAttempt attempt) {
        int newCount = attempt.getSmsCodeAttemptsCount()+1;
        long modifiedCount = resetPasswordAttemptCollection.updateOne(
                and(byId(attempt.getId()), eq("attemptsCount", attempt.getSmsCodeAttemptsCount())),
                set("attemptsCount", newCount)
        ).getModifiedCount();

        if(0 == modifiedCount){
            final ResetPasswordAttempt refreshedAttempt = findById(attempt.getId());
            return increaseSmsCodeAttemptsCount(refreshedAttempt);
        } else {
            return newCount;
        }
    }


}
