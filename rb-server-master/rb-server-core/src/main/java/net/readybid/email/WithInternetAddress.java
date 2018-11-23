package net.readybid.email;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface WithInternetAddress {
    InternetAddress getInternetAddress() throws UnsupportedEncodingException;
}
