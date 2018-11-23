package net.readybid.templates;

import java.io.IOException;
import java.util.Map;

/**
 * Created by DejanK on 8/27/2015.
 *
 */
public interface TemplateService {

    String SIMPLE_TEMPLATE = "/templates/simple-email.html";
    String SIMPLE_TEMPLATE_WITHOUT_LINK = "/templates/simple-email-without-link.html";
    String CONTACT_US_TEMPLATE = "/templates/contact-us-email.html";

    String parseTemplate(String templatePath, Map<String, String> model) throws IOException;
}
