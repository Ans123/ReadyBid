package net.readybid.auth.login;

public interface GetActiveUserPasswordRepository {
    String getActiveUserPasswordByEmailAddress(String emailAddress);
}
