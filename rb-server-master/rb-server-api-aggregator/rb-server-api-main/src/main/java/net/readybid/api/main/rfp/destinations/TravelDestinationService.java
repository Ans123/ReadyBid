package net.readybid.api.main.rfp.destinations;

import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
public interface TravelDestinationService {

    TravelDestination getRfpTravelDestination(String rfpId, String destinationId);

    ListResult<TravelDestinationListItemViewModel> listUserTravelDestinationsWithRfpName(AuthenticatedUser user);

    ListResult<TravelDestinationListItemViewModel> listRfpTravelDestinationsWithRfpName(String rfpId);
}
