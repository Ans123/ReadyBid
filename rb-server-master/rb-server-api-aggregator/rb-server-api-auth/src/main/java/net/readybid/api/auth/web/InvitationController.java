package net.readybid.api.auth.web;

import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationService;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.*;
import net.readybid.web.ActionResponse;
import net.readybid.web.GetResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by DejanK on 3/20/2017.
 *
 */
@RestController
@RequestMapping(value = "/invitation")
public class InvitationController {

    private final AuthFacade authFacade;
    private final InvitationService invitationService;

    @Autowired
    public InvitationController(AuthFacade authFacade, InvitationService invitationService) {
        this.authFacade = authFacade;
        this.invitationService = invitationService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public GetResponse<Invitation, InvitationViewModel> getTokenUser (
            @RequestParam(value = "token") String token
    ) {
        final GetResponse<Invitation, InvitationViewModel> response = new GetResponse<>();
        final Invitation invitation = invitationService.getInvitation(token);
        final InvitationViewModel invitationViewModel = InvitationViewModel.FACTORY.createView(invitation);
        invitationViewModel.isUser = authFacade.isUser(invitation.getEmailAddress());
        return response.finalizeResult(invitationViewModel);
    }


    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse signUpWithInvitation(
            @Valid @RequestBody SignUpWithInvitationRequest signUpWithInvitationRequest,
            HttpServletResponse httpServletResponse
    ) {
        final ActionResponse actionResult = new ActionResponse();
        final ObjectId userAccountId = authFacade.signUpWithInvitation(httpServletResponse, signUpWithInvitationRequest);
        return actionResult.finalizeAction("userAccountId", String.valueOf(userAccountId));
    }

    @RequestMapping(value = "/add-account", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse addAccountWithInvitation(
            @Valid @RequestBody AddAccountWithInvitationRequest addAccountWithInvitationRequest,
            HttpServletResponse httpServletResponse
    ) {
        final ActionResponse actionResult = new ActionResponse();
        final ObjectId userAccountId = authFacade.addAccountWithInvitation(httpServletResponse, addAccountWithInvitationRequest);
        return actionResult.finalizeAction("userAccountId", String.valueOf(userAccountId));
    }
}
