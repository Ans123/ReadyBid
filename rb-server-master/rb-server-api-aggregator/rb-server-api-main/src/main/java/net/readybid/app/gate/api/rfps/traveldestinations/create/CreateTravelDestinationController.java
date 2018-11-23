package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.service.traveldestination.CreateTravelDestinationService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.ActionResponse;
import net.readybid.web.WriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CreateTravelDestinationController {

    private final CreateTravelDestinationService destinationTransaction;

    @Autowired
    public CreateTravelDestinationController( CreateTravelDestinationService createTravelDestinationTransaction ) {
        this.destinationTransaction = createTravelDestinationTransaction;
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/office", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public WriteResponse createOfficeTravelDestination(
            @RequestBody @Valid CreateTravelDestinationOfficeRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        return createDestination(requestData.getCreateModel(rfpId, user), request, response);
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/city", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public WriteResponse createCityTravelDestination(
            @RequestBody @Valid CreateTravelDestinationCityRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
            return createDestination(requestData.getCreateModel(rfpId, user), request, response);
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/bulk", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ActionResponse createTravelDestinationsInBulk(
            @RequestBody @Valid CreateTravelDestinationBulkRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user
    ) {
        final ActionResponse actionResponse = new ActionResponse();
        final List<String> travelDestinationIds =
                destinationTransaction.createAll(rfpId, requestData.getModels(rfpId, user));
        return actionResponse.finalizeAction("ids", travelDestinationIds);
    }

    private WriteResponse createDestination(
            CreateTravelDestinationRequest model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final WriteResponse writeResponse = new WriteResponse(request, response);
        final TravelDestination travelDestination = destinationTransaction.create(model);
        return writeResponse.finalizeResponse(null, travelDestination.getId());
    }
}
