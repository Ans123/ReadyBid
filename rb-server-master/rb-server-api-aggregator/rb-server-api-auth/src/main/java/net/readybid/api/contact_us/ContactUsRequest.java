package net.readybid.api.contact_us;

import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.validators.RbEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 12/12/2016.
 *
 */
@SuppressWarnings("WeakerAccess")
public class ContactUsRequest {

    @NotBlank
    @RbEmail
    public String emailAddress;

    @NotBlank
    @Size(max = 100)
    public String name;

    @NotBlank
    @Size(max = 10000)
    public String message;

    ContactUsMessage toContactUsMessage() {
        return new ContactUsMessage(name, emailAddress, message);
    }
}
