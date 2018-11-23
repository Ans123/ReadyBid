package net.readybid.auth.password;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface PasswordService {

    String encode(String password);

    boolean equals(String password1, String password2);
}
