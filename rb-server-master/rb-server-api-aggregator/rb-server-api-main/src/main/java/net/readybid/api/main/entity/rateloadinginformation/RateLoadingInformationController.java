package net.readybid.api.main.entity.rateloadinginformation;

import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.web.GetResponse;
import net.readybid.web.WriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by DejanK on 10/3/2017.
 *
 */
@RestController
@RequestMapping(value = "/entities/")
public class RateLoadingInformationController {

    private final RateLoadingInformationFacade rateLoadingInformationFacade;

    @Autowired
    public RateLoadingInformationController(RateLoadingInformationFacade rateLoadingInformationFacade) {
        this.rateLoadingInformationFacade = rateLoadingInformationFacade;
    }

    @RequestMapping(value = "{entityId}/rate-loading-information", method = RequestMethod.GET)
    public GetResponse<EntityRateLoadingInformation, EntityRateLoadingInformationView> getRateLoadingInformation(
            @PathVariable(value = "entityId") String entityId
    ) {
        final GetResponse<EntityRateLoadingInformation, EntityRateLoadingInformationView> response = new GetResponse<>();
        final EntityRateLoadingInformation information = rateLoadingInformationFacade.get(entityId);
        return response.finalizeResult(information, EntityRateLoadingInformationView.FACTORY);
    }

    @RequestMapping(value = "{entityId}/rate-loading-information", method = RequestMethod.PUT)
    public WriteResponse putRateLoadingInformation(
            @PathVariable(value = "entityId") String entityId,
            @RequestBody @Valid UpdateEntityRateLoadingInformationRequest updateInformationRequest,
            HttpServletResponse servletResponse
    ) {
        final WriteResponse response =
                new WriteResponse("entities/%s/rate-loading-information", servletResponse);
        rateLoadingInformationFacade.update(entityId, updateInformationRequest);
        return response.finalizeResponse(entityId);
    }
}
