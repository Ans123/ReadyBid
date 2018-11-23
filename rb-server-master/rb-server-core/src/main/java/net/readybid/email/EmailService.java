package net.readybid.email;

import javax.mail.internet.InternetAddress;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
public interface EmailService {
    Map<String, String> prepareModel();
    InternetAddress getOfficialSender();
    void send(Email email);
}
