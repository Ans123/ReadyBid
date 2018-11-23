package net.readybid.api.contact_us;

import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.validators.RbEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 2/28/2017.
 *
 */
@SuppressWarnings("WeakerAccess")
public class CustomRfpMessageRequest {

    @NotBlank
    @Size(max = 100)
    public String firstName;

    @NotBlank
    @Size(max = 100)
    public String lastName;

    @NotBlank
    @RbEmail
    public String emailAddress;

    @Size(max = 100)
    public String phone;

    @NotBlank
    @Size(max = 100)
    public String company;


    @NotBlank
    @Size(max = 10000)
    public String message;

    CustomRfpMessage toCustomRfpMessage() {
        return new CustomRfpMessage(firstName, lastName, emailAddress, phone, company, message);
    }
}
