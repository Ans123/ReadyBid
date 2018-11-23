package net.readybid.api.main.rfp.hotel.traveldestination.delete;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.core.rfp.hotel.traveldestination.*;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.web.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteTravelDestinationController {

    private final RfpAccessControlService rfpAccessControlService;
    private final DeleteTravelDestinationTransaction destinationTransaction;

    @Autowired
    public DeleteTravelDestinationController(
            RfpAccessControlService rfpAccessControlService,
            DeleteTravelDestinationTransaction destinationTransaction
    ) {
        this.rfpAccessControlService = rfpAccessControlService;
        this.destinationTransaction = destinationTransaction;
    }

    @RequestMapping(value = "/rfps/{rfpId}/travel-destinations/{destinationId}", method = RequestMethod.DELETE)
    public ActionResponse deleteTravelDestination(
            @PathVariable(value = "rfpId") String rfpId,
            @PathVariable(value = "destinationId") String destinationId,
            @CurrentUser AuthenticatedUser user
    ) {
        rfpAccessControlService.delete(rfpId);
        final ActionResponse ac = new ActionResponse();
        final DeleteTravelDestinationRequest requestModel = new DeleteTravelDestinationRequest(destinationId, user);
        final DeleteTravelDestinationResult deleteResult = destinationTransaction.delete(requestModel);

        ac.put("success", deleteResult.successCount);
        ac.put("failure", deleteResult.failCount);
        return ac.finalizeAction(deleteResult.failCount == 0 ? "OK" : "FAILED");
    }
}
