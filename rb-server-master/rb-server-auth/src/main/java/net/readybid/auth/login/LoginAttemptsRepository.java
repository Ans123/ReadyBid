package net.readybid.auth.login;

import java.util.List;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
public interface LoginAttemptsRepository {
    void create(LoginAttempt passwordAttempt, LoginAttempt emailAttempt);

    void purgeOldAttempts(long attemptsMaxAge);

    long uniqueAttemptsCount();

    List<LoginAttempt> getAttempts(String hashedEmailAddress, String hashedPassword);
}
