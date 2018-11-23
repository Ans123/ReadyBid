package net.readybid.api.rfp_hotel.rfp;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.use_cases.rfp_hotel.rfp.HotelRfpSpecificationsHandler;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.web.RbResponseView;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/rfps/hotel/")
public class HotelRfpSpecificationController {

    private final RfpAccessControlService rfpAccessControlService;
    private final HotelRfpSpecificationsHandler hotelRfpSpecificationsHandler;

    @Autowired
    public HotelRfpSpecificationController(
            RfpAccessControlService rfpAccessControlService,
            HotelRfpSpecificationsHandler hotelRfpSpecificationsHandler
    ){
        this.rfpAccessControlService = rfpAccessControlService;
        this.hotelRfpSpecificationsHandler = hotelRfpSpecificationsHandler;
    }

    @RbResponseView
    @PostMapping(value = "{rfpId}/chain-support")
    @ResponseStatus(HttpStatus.OK)
    public RbViewModel setHotelRfpChainSupport(
            @PathVariable(value = "rfpId") String rfpId,
            @Valid @RequestBody EnableChainSupportRequest request
    ) {
        rfpAccessControlService.update(rfpId);
        if(!request.enableChainSupport) throw new UnableToCompleteRequestException("Not Supported");
        return hotelRfpSpecificationsHandler.enableChainSupport(rfpId);
    }

    @SuppressWarnings("WeakerAccess")
    public static class EnableChainSupportRequest {

        @NotNull
        public Boolean enableChainSupport;
    }
}
