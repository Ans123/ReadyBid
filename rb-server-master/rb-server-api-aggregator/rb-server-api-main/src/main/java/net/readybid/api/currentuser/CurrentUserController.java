package net.readybid.api.currentuser;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.app.use_cases.authentication.user.UpdateUser;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import net.readybid.web.RbViewModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class CurrentUserController {

    private final UpdateUser updateUser;

    @Autowired
    public CurrentUserController(UpdateUser updateUser) {
        this.updateUser = updateUser;
    }

    @RbResponseView
    @PutMapping(value = "/current-user/basic-info")
    public RbViewModel updateBasicInformation(
            @CurrentUser AuthenticatedUser user,
            @RequestBody @Valid UpdateBasicInformationRequest request
    ){
        if(request.hasDifferences(user)){
            final BasicInformation basicInformation = request.toBasicInformation();
            updateUser.setBasicInformation(String.valueOf(user.getId()), basicInformation);
        }
        return RbViewModels.ACTION_SUCCESS;
    }

    @RbResponseView
    @PutMapping(value = "/current-user/email-address")
    public RbViewModel updateEmailAddress(
            @RequestBody @Valid UpdateEmailAddressRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        updateUser.setEmailAddress(String.valueOf(currentUser.getId()), request.getEmailAddress());
        return RbViewModels.ACTION_SUCCESS;
    }

    @RbResponseView
    @PutMapping(value = "/current-user/password")
    public RbViewModel updatePassword(
            @RequestBody @Valid UpdatePasswordRequest request,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        updateUser.setPassword(String.valueOf(currentUser.getId()), request.getPassword());
        return RbViewModels.ACTION_SUCCESS;
    }

    @RbResponseView
    @PostMapping(value = "/current-user/profile-picture")
    public RbViewModel updateProfilePicture(
            @RequestPart("file") MultipartFile file,
            @CurrentUser AuthenticatedUser currentUser
    ) {
        updateUser.setProfilePicture(currentUser, file);
        return RbViewModels.ACTION_SUCCESS;
    }
}
