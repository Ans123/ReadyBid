package net.readybid.app.entities.email;

import javax.mail.internet.InternetAddress;
import java.util.Map;

public interface EmailTemplate {

    String getId();

    InternetAddress getReceiver();

    InternetAddress[] getCC();

    String getSubject();

    String getHtmlTemplateName();

    Map<String,String> getModel();
}
