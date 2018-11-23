package net.readybid.api.main.rfp.destinations;

import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
@Service
public class TravelDestinationServiceImpl implements TravelDestinationService {

    private final TravelDestinationsRepository destinationsRepository;

    @Autowired
    public TravelDestinationServiceImpl(
            TravelDestinationsRepository destinationsRepository
    ) {
        this.destinationsRepository = destinationsRepository;
    }

    @Override
    public TravelDestination getRfpTravelDestination(String rfpId, String destinationId) {
        return destinationsRepository.getById(destinationId, rfpId);
    }

    @Override
    public ListResult<TravelDestinationListItemViewModel> listUserTravelDestinationsWithRfpName(AuthenticatedUser user) {
        return destinationsRepository.listUserTravelDestinationsWithRfpName(user);
    }

    @Override
    public ListResult<TravelDestinationListItemViewModel> listRfpTravelDestinationsWithRfpName(String rfpId) {
        return destinationsRepository.listRfpTravelDestinationsWithRfpName(rfpId);
    }
}
