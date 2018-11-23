package net.readybid.auth.login;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.mongodb.MongoRetry;
import net.readybid.mongodb.collections.LoginAttemptCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lt;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
@Service
public class LoginAttemptsRepositoryImpl implements LoginAttemptsRepository {

    private final MongoCollection<LoginAttempt> loginAttemptCollection;

    @Autowired
    public LoginAttemptsRepositoryImpl(MongoDatabase mongoDatabase) {
        loginAttemptCollection = mongoDatabase.getCollection(LoginAttemptCollection.COLLECTION_NAME, LoginAttempt.class);
    }

    @Override
    @MongoRetry
    public void create(LoginAttempt passwordAttempt, LoginAttempt emailAttempt) {
        final List<LoginAttempt> attempts = new ArrayList<>();
        attempts.add(emailAttempt);
        attempts.add(passwordAttempt);
        loginAttemptCollection.insertMany(attempts);
    }

    @Override
    @MongoRetry
    public void purgeOldAttempts(long attemptsMaxAge) {
        loginAttemptCollection.deleteMany(
                lt("at", new Date().getTime() - attemptsMaxAge)
        );
    }

    @Override
    @MongoRetry
    public long uniqueAttemptsCount() {
        return loginAttemptCollection.count();
    }

    @Override
    @MongoRetry
    public List<LoginAttempt> getAttempts(String hashedEmailAddress, String hashedPassword) {
        final List<LoginAttempt> attempts = new ArrayList<>();
        loginAttemptCollection.find(
                in("target", hashedEmailAddress, hashedPassword)
        ).into(attempts);
        return attempts;
    }
}
