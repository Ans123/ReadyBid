package net.readybid.api.currentuser;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.auth.user.User;
import net.readybid.utils.Utils;
import net.readybid.validators.Text20;
import net.readybid.validators.Text50;

import javax.validation.constraints.NotNull;

@SuppressWarnings("WeakerAccess")
public class UpdateBasicInformationRequest {

    @NotNull
    @Text50
    public String firstName;

    @NotNull
    @Text50
    public String lastName;

    @Text20
    public String phone;

    BasicInformation toBasicInformation() {
        final BasicInformation to = new BasicInformation();
        to.firstName = firstName;
        to.lastName = lastName;
        to.phone = phone;
        return to;
    }

    boolean hasDifferences(User user) {
        boolean hasDifferences = !Utils.areEqual(firstName, user.getFirstName());
        hasDifferences = hasDifferences || !Utils.areEqual(lastName, user.getLastName());
        hasDifferences = hasDifferences || !Utils.areEqual(phone, user.getEmailAddress());
        return hasDifferences;
    }
}
