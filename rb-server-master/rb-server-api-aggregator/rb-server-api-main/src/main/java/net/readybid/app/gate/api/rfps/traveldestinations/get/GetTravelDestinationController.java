package net.readybid.app.gate.api.rfps.traveldestinations.get;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.api.main.bidmanager.core.BidManagerFacade;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.service.traveldestination.GetTravelDestinationService;
import net.readybid.app.core.service.traveldestination.ListTravelDestinationsResult;
import net.readybid.app.use_cases.rfp_hotel.travel_destination.ListTravelDestination;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.rfphotel.destinations.web.TravelDestinationViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetTravelDestinationController {

    private final GetTravelDestinationService destinationService;
    private final BidManagerFacade bidManagerFacade;
    private final RfpAccessControlService rfpAccessControlService;
    private final ListTravelDestination listTravelDestination;

    @Autowired
    public GetTravelDestinationController(
            GetTravelDestinationService destinationService,
            BidManagerFacade bidManagerFacade,
            RfpAccessControlService rfpAccessControlService,
            ListTravelDestination listTravelDestination
    ) {
        this.destinationService = destinationService;
        this.bidManagerFacade = bidManagerFacade;
        this.rfpAccessControlService = rfpAccessControlService;
        this.listTravelDestination = listTravelDestination;
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations", method = RequestMethod.GET)
    public ListResponse<TravelDestinationListItemView> listTravelDestinations(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final ListResponse<TravelDestinationListItemView> listResponse = new ListResponse<>();
        final ListTravelDestinationsResult result = destinationService.listRfpDestinations(rfpId);

        final List<TravelDestinationListItemView> viewList =
                TravelDestinationListItemView.fromList(result.destinations, result.bidsPerTravelDestination);

        return listResponse.finalizeResult(viewList);
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/{destinationId}", method = RequestMethod.GET)
    public GetResponse<TravelDestination, TravelDestinationViewModel> viewTravelDestination(
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId
    ) {
        final GetResponse<TravelDestination, TravelDestinationViewModel> getResponse = new GetResponse<>();
        final TravelDestination destination = destinationService.get(rfpId, destinationId);
        return getResponse.finalizeResult(destination, TravelDestinationViewModel.FACTORY);
    }

    @RbResponseView
    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/{destinationId}/properties", method = RequestMethod.GET)
    public RbViewModel listTravelDestinationProperties(
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId
    ) {
        rfpAccessControlService.read(rfpId);
        return listTravelDestination.listProperties(rfpId, destinationId);
    }

    @RequestMapping(value = "/bid-managers/travel-destinations/rfps/{rfpId}", method = RequestMethod.GET)
    public ListResponse<TravelDestinationListItemViewModel> listRfpTravelDestinations(
            @PathVariable(value = "rfpId") String rfpId
    ) {
        final ListResponse<TravelDestinationListItemViewModel> listResponse = new ListResponse<>();

        final ListResult<TravelDestinationListItemViewModel> listResult = bidManagerFacade.listRfpTravelDestinations(rfpId);
        return listResponse.finalizeResult(listResult);
    }

    @RequestMapping(value = "/bid-managers/travel-destinations", method = RequestMethod.GET)
    public ListResponse<TravelDestinationListItemViewModel> listTravelDestinations(
            @CurrentUser AuthenticatedUser user
    ) {
        final ListResponse<TravelDestinationListItemViewModel> listResponse = new ListResponse<>();

        final ListResult<TravelDestinationListItemViewModel> listResult = bidManagerFacade.listUserTravelDestinations(user);
        return listResponse.finalizeResult(listResult);
    }
}
