package net.readybid.rfphotel.letter;

import net.readybid.web.HtmlInputSecurityPolicy;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 3/1/2017.
 *
 */
public class UpdateLetterTemplateRequest {

    @NotNull
    @Size(max = 1100000)
    public String template;

    public String getSanitizedTemplate(){
        final String sanitized = HtmlInputSecurityPolicy.POLICY_DEFINITION.sanitize(template);
        return sanitized.replaceAll("\\{<!-- -->\\{", "{{");
    }
}
