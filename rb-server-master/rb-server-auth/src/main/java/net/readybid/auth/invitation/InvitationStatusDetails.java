package net.readybid.auth.invitation;

import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 5/21/2017.
 *
 */
public class InvitationStatusDetails extends StatusDetails<InvitationStatus> {

    public InvitationStatusDetails() {}

    public InvitationStatusDetails(CreationDetails creationDetails) {
        super(creationDetails, InvitationStatus.CREATED);
    }

    public InvitationStatusDetails(InvitationStatus status, BasicUserDetails userDetails) {
        super(userDetails, status);
    }
}
