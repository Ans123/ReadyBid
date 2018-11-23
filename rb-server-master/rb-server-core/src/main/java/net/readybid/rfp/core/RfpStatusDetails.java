package net.readybid.rfp.core;

import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class RfpStatusDetails extends StatusDetails<RfpStatus> {

    public RfpStatusDetails() {}

    public RfpStatusDetails(CreationDetails created, RfpStatus status) {
        super(created, status);
    }

    public RfpStatusDetails(RfpStatus status, BasicUserDetails userDetails) {
        super(userDetails, status);
    }
}
