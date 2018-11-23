package net.readybid.app.core.entities.traveldestination;

import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;
import net.readybid.user.BasicUserDetails;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class TravelDestinationStatusDetails extends StatusDetails<TravelDestinationStatus> {

    public TravelDestinationStatusDetails(){}

    public TravelDestinationStatusDetails(CreationDetails creationDetails) {
        super(creationDetails, TravelDestinationStatus.CREATED);
    }

    public TravelDestinationStatusDetails(BasicUserDetails user, TravelDestinationStatus statusValue) {
        super(user, statusValue);
    }

    public TravelDestinationStatusDetails(TravelDestinationStatus status, BasicUserDetails user) {
        super(user, status);
    }
}
