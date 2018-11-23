package net.readybid.api.main.rfp.hotel.traveldestination.update;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationTransaction;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationRequest;
import net.readybid.app.gate.api.rfps.traveldestinations.create.CreateTravelDestinationCityRequest;
import net.readybid.app.gate.api.rfps.traveldestinations.create.CreateTravelDestinationOfficeRequest;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.web.ActionResponse;
import net.readybid.web.WriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class UpdateTravelDestinationController {

    private final RfpAccessControlService rfpAccessControlService;
    private final UpdateTravelDestinationTransaction travelDestinationModifier;

    @Autowired
    public UpdateTravelDestinationController(
            RfpAccessControlService rfpAccessControlService,
            UpdateTravelDestinationTransaction travelDestinationModifier
    ) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.travelDestinationModifier = travelDestinationModifier;
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/office/{destinationId}", method = RequestMethod.PUT)
    public WriteResponse updateTravelDestinationOffice(
            @RequestBody @Valid CreateTravelDestinationOfficeRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        return updateDestination(rfpId, requestData.getUpdateModel(rfpId, destinationId, user), request, response);
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/city/{destinationId}", method = RequestMethod.PUT)
    public WriteResponse updateTravelDestinationCity(
            @RequestBody @Valid CreateTravelDestinationCityRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId,
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        return updateDestination(rfpId, requestData.getUpdateModel(rfpId, destinationId, user), request, response);
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/{destinationId}/filter", method = RequestMethod.PUT)
    public ActionResponse saveTravelDestinationFilter(
            @RequestBody @Valid UpdateTravelDestinationFilterWebRequest requestData,
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId
    ) {
        final ActionResponse ac = new ActionResponse();
        rfpAccessControlService.update(rfpId);
        travelDestinationModifier.updateFilter(requestData.getModel(destinationId));
        return ac.finalizeAction();
    }

    private WriteResponse updateDestination(String rfpId, UpdateTravelDestinationRequest model, HttpServletRequest request, HttpServletResponse response) {
        final WriteResponse writeResponse = new WriteResponse(request, response);
        rfpAccessControlService.update(rfpId);
        final TravelDestination travelDestination = travelDestinationModifier.update(model);
        return writeResponse.finalizeResponse(null, travelDestination.getId());
    }
}
